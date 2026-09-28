const API_BASE = "/api";

document.addEventListener("DOMContentLoaded", () => {
    loadDashboard();
});


/* =========================================
   GENERAL API FUNCTION
========================================= */

async function apiRequest(url, options = {}) {

    const config = {
        ...options,

        headers: {
            "Content-Type": "application/json",
            ...(options.headers || {})
        }
    };

    const response = await fetch(url, config);

    const text = await response.text();

    let data = null;

    if (text) {
        try {
            data = JSON.parse(text);
        } catch {
            data = text;
        }
    }

    if (!response.ok) {

        let message = "Request failed";

        if (data && data.message) {
            message = data.message;
        } else if (data && data.messages) {
            message = Object.values(data.messages).join(", ");
        } else if (typeof data === "string") {
            message = data;
        }

        throw new Error(message);
    }

    return data;
}


/* =========================================
   MESSAGE
========================================= */

function showMessage(message) {

    const messageBox =
        document.getElementById("messageBox");

    messageBox.textContent = message;
    messageBox.classList.remove("hidden");

    clearTimeout(window.messageTimer);

    window.messageTimer = setTimeout(() => {
        messageBox.classList.add("hidden");
    }, 4000);
}


/* =========================================
   SAFE HTML
========================================= */

function escapeHtml(value) {

    if (value === null || value === undefined) {
        return "";
    }

    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}


/* =========================================
   DASHBOARD
========================================= */

async function loadDashboard() {

    try {

        await Promise.all([
            loadSchemes(),
            loadStudents(),
            loadApplications()
        ]);

    } catch (error) {

        console.error(error);

        showMessage(error.message);
    }
}


async function refreshDashboard() {

    await loadDashboard();

    showMessage("Dashboard refreshed successfully");
}


/* =========================================
   SCHEME MODULE
========================================= */

async function loadSchemes() {

    const schemes =
        await apiRequest(`${API_BASE}/schemes`);

    document.getElementById("schemeCount").textContent =
        schemes.length;

    renderSchemeTable(schemes);

    populateSchemeSelect(schemes);
}


function renderSchemeTable(schemes) {

    const tableBody =
        document.getElementById("schemeTableBody");

    tableBody.innerHTML = "";

    if (schemes.length === 0) {

        tableBody.innerHTML = `
            <tr>
                <td colspan="5">
                    No scholarship schemes found.
                </td>
            </tr>
        `;

        return;
    }

    schemes.forEach(scheme => {

        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${escapeHtml(scheme.id)}</td>

            <td>${escapeHtml(scheme.name)}</td>

            <td>
                ₹${Number(scheme.incomeLimit)
            .toLocaleString("en-IN")}
            </td>

            <td>
                ${escapeHtml(scheme.minimumMarks)}
            </td>

            <td>
                <span class="badge">
                    ${scheme.active ? "ACTIVE" : "INACTIVE"}
                </span>
            </td>
        `;

        tableBody.appendChild(row);
    });
}


function populateSchemeSelect(schemes) {

    const select =
        document.getElementById("applicationScheme");

    select.innerHTML = `
        <option value="">
            Select Scheme
        </option>
    `;

    const activeSchemes =
        schemes.filter(scheme => scheme.active);

    activeSchemes.forEach(scheme => {

        const option =
            document.createElement("option");

        option.value = scheme.id;

        option.textContent =
            `${scheme.name} - Min marks: ${scheme.minimumMarks}`;

        select.appendChild(option);
    });
}


/* =========================================
   STUDENT MODULE
========================================= */

async function loadStudents() {

    const students =
        await apiRequest(`${API_BASE}/students`);

    document.getElementById("studentCount").textContent =
        students.length;

    renderStudentTable(students);

    populateStudentSelect(students);
}


function renderStudentTable(students) {

    const tableBody =
        document.getElementById("studentTableBody");

    tableBody.innerHTML = "";

    if (students.length === 0) {

        tableBody.innerHTML = `
            <tr>
                <td colspan="5">
                    No students found.
                </td>
            </tr>
        `;

        return;
    }

    students.forEach(student => {

        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${escapeHtml(student.id)}</td>

            <td>${escapeHtml(student.name)}</td>

            <td>${escapeHtml(student.email)}</td>

            <td>
                ₹${Number(student.annualIncome)
            .toLocaleString("en-IN")}
            </td>

            <td>${escapeHtml(student.marks)}</td>
        `;

        tableBody.appendChild(row);
    });
}


function populateStudentSelect(students) {

    const select =
        document.getElementById("applicationStudent");

    select.innerHTML = `
        <option value="">
            Select Student
        </option>
    `;

    students.forEach(student => {

        const option =
            document.createElement("option");

        option.value = student.id;

        option.textContent =
            `${student.name} - ${student.email}`;

        select.appendChild(option);
    });
}


/* =========================================
   APPLICATION MODULE
========================================= */

let applicationCache = [];


async function loadApplications() {

    const applications =
        await apiRequest(`${API_BASE}/applications`);

    applicationCache = applications;

    document.getElementById("applicationCount").textContent =
        applications.length;

    const pendingReviews =
        applications.filter(
            application =>
                application.applicationStatus === "UNDER_REVIEW"
        );

    const disbursed =
        applications.filter(
            application =>
                application.disbursementStatus === "COMPLETED"
        );

    document.getElementById("pendingCount").textContent =
        pendingReviews.length;

    document.getElementById("disbursedCount").textContent =
        disbursed.length;

    renderApplicationTable(applications);

    populateVerificationSelect(pendingReviews);

    populateDisbursementSelect(applications);
}


function renderApplicationTable(applications) {

    const tableBody =
        document.getElementById("applicationTableBody");

    tableBody.innerHTML = "";

    if (applications.length === 0) {

        tableBody.innerHTML = `
            <tr>
                <td colspan="6">
                    No applications found.
                </td>
            </tr>
        `;

        return;
    }

    applications.forEach(application => {

        const studentName =
            application.student?.name || "Unknown";

        const schemeName =
            application.scheme?.name || "Unknown";

        const eligibilityText =
            application.eligibilityPassed
                ? "PASSED"
                : "FAILED";

        const row =
            document.createElement("tr");

        row.innerHTML = `
            <td>#${escapeHtml(application.id)}</td>

            <td>${escapeHtml(studentName)}</td>

            <td>${escapeHtml(schemeName)}</td>

            <td>
                <span class="badge">
                    ${eligibilityText}
                </span>
            </td>

            <td>
                <span class="badge">
                    ${escapeHtml(application.applicationStatus)}
                </span>
            </td>

            <td>
                <span class="badge">
                    ${escapeHtml(application.disbursementStatus)}
                </span>
            </td>
        `;

        tableBody.appendChild(row);
    });
}


function populateVerificationSelect(applications) {

    const select =
        document.getElementById(
            "verificationApplication"
        );

    select.innerHTML = `
        <option value="">
            Select Application
        </option>
    `;

    applications.forEach(application => {

        const studentName =
            application.student?.name || "Student";

        const schemeName =
            application.scheme?.name || "Scheme";

        const option =
            document.createElement("option");

        option.value = application.id;

        option.textContent =
            `#${application.id} - ${studentName} - ${schemeName}`;

        select.appendChild(option);
    });
}


function populateDisbursementSelect(applications) {

    const select =
        document.getElementById(
            "disbursementApplication"
        );

    select.innerHTML = `
        <option value="">
            Select Application
        </option>
    `;

    const verifiedApplications =
        applications.filter(
            application =>
                application.applicationStatus === "VERIFIED"
        );

    verifiedApplications.forEach(application => {

        const studentName =
            application.student?.name || "Student";

        const option =
            document.createElement("option");

        option.value = application.id;

        option.textContent =
            `#${application.id} - ${studentName}`;

        select.appendChild(option);
    });
}


/* =========================================
   CREATE SCHEME
========================================= */

document.getElementById("schemeForm")
    .addEventListener("submit", async event => {

        event.preventDefault();

        const schemeName =
            document.getElementById("schemeName")
                .value
                .trim();

        const description =
            document.getElementById("schemeDescription")
                .value
                .trim();

        const incomeLimit =
            Number(
                document.getElementById("incomeLimit")
                    .value
            );

        const minimumMarks =
            Number(
                document.getElementById("minimumMarks")
                    .value
            );

        if (!schemeName || !description) {
            showMessage("Please fill all scheme fields.");
            return;
        }

        if (incomeLimit < 0) {
            showMessage("Income limit cannot be negative.");
            return;
        }

        if (minimumMarks < 0 || minimumMarks > 100) {
            showMessage("Minimum marks must be between 0 and 100.");
            return;
        }

        const scheme = {
            name: schemeName,
            description: description,
            incomeLimit: incomeLimit,
            minimumMarks: minimumMarks,
            active: true
        };

        try {

            await apiRequest(
                `${API_BASE}/schemes`,
                {
                    method: "POST",
                    body: JSON.stringify(scheme)
                }
            );

            event.target.reset();

            showMessage(
                "Scholarship scheme created successfully."
            );

            await loadDashboard();

        } catch (error) {

            showMessage(error.message);
        }
    });


/* =========================================
   CREATE STUDENT
========================================= */

document.getElementById("studentForm")
    .addEventListener("submit", async event => {

        event.preventDefault();

        const name =
            document.getElementById("studentName")
                .value
                .trim();

        const email =
            document.getElementById("studentEmail")
                .value
                .trim();

        const annualIncome =
            Number(
                document.getElementById("annualIncome")
                    .value
            );

        const marks =
            Number(
                document.getElementById("studentMarks")
                    .value
            );

        if (!name || !email) {
            showMessage("Please enter name and email.");
            return;
        }

        if (annualIncome < 0) {
            showMessage("Annual income cannot be negative.");
            return;
        }

        if (marks < 0 || marks > 100) {
            showMessage("Marks must be between 0 and 100.");
            return;
        }

        const student = {
            name: name,
            email: email,
            annualIncome: annualIncome,
            marks: marks
        };

        try {

            await apiRequest(
                `${API_BASE}/students`,
                {
                    method: "POST",
                    body: JSON.stringify(student)
                }
            );

            event.target.reset();

            showMessage(
                "Student registered successfully."
            );

            await loadDashboard();

        } catch (error) {

            showMessage(error.message);
        }
    });


/* =========================================
   CREATE APPLICATION
========================================= */

document.getElementById("applicationForm")
    .addEventListener("submit", async event => {

        event.preventDefault();

        const studentId =
            Number(
                document.getElementById(
                    "applicationStudent"
                ).value
            );

        const schemeId =
            Number(
                document.getElementById(
                    "applicationScheme"
                ).value
            );

        if (!studentId || !schemeId) {
            showMessage(
                "Please select both student and scholarship scheme."
            );
            return;
        }

        const request = {
            studentId: studentId,
            schemeId: schemeId
        };

        try {

            const application =
                await apiRequest(
                    `${API_BASE}/applications`,
                    {
                        method: "POST",
                        body: JSON.stringify(request)
                    }
                );

            event.target.reset();

            showApplicationResult(application);

            showMessage(
                "Scholarship application submitted successfully."
            );

            await loadDashboard();

        } catch (error) {

            showMessage(error.message);
        }
    });


function showApplicationResult(application) {

    const result =
        document.getElementById("applicationResult");

    result.classList.remove("hidden");

    const eligibility =
        application.eligibilityPassed
            ? "PASSED"
            : "FAILED";

    result.innerHTML = `
        <h3>
            Application #${escapeHtml(application.id)}
        </h3>

        <p>
            <strong>Eligibility:</strong>
            ${eligibility}
        </p>

        <p>
            <strong>Status:</strong>
            ${escapeHtml(application.applicationStatus)}
        </p>

        <p>
            <strong>Message:</strong>
            ${escapeHtml(application.eligibilityMessage)}
        </p>
    `;
}


/* =========================================
   VERIFICATION
========================================= */

document.getElementById("verificationForm")
    .addEventListener("submit", async event => {

        event.preventDefault();

        const applicationId =
            Number(
                document.getElementById(
                    "verificationApplication"
                ).value
            );

        const verifierName =
            document.getElementById("verifierName")
                .value
                .trim();

        const decision =
            document.getElementById("decision")
                .value;

        const remarks =
            document.getElementById("remarks")
                .value
                .trim();

        if (!applicationId) {
            showMessage("Please select an application.");
            return;
        }

        if (!verifierName || !decision || !remarks) {
            showMessage(
                "Please complete all verification fields."
            );
            return;
        }

        const request = {
            verifierName: verifierName,
            decision: decision,
            remarks: remarks
        };

        try {

            await apiRequest(
                `${API_BASE}/applications/${applicationId}/verification`,
                {
                    method: "POST",
                    body: JSON.stringify(request)
                }
            );

            event.target.reset();

            showMessage(
                `Application #${applicationId} verification completed.`
            );

            await loadDashboard();

        } catch (error) {

            showMessage(error.message);
        }
    });


/* =========================================
   DISBURSEMENT
========================================= */

document.getElementById("disbursementForm")
    .addEventListener("submit", async event => {

        event.preventDefault();

        const applicationId =
            Number(
                document.getElementById(
                    "disbursementApplication"
                ).value
            );

        if (!applicationId) {
            showMessage("Please select an application.");
            return;
        }

        try {

            await apiRequest(
                `${API_BASE}/applications/${applicationId}/disbursement`,
                {
                    method: "PUT",
                    body: JSON.stringify({
                        status: "COMPLETED"
                    })
                }
            );

            event.target.reset();

            showMessage(
                `Application #${applicationId} disbursement completed.`
            );

            await loadDashboard();

        } catch (error) {

            showMessage(error.message);
        }
    });


/* =========================================
   APPLICATION STATUS
========================================= */

async function loadApplicationStatus() {

    const input =
        document.getElementById(
            "statusApplicationId"
        );

    const applicationId =
        Number(input.value);

    if (!applicationId) {

        showMessage(
            "Please enter a valid application ID."
        );

        return;
    }

    try {

        const application =
            await apiRequest(
                `${API_BASE}/applications/${applicationId}`
            );

        renderApplicationStatus(application);

    } catch (error) {

        document.getElementById(
            "statusResult"
        ).innerHTML = "";

        showMessage(error.message);
    }
}


function renderApplicationStatus(application) {

    const result =
        document.getElementById("statusResult");

    const studentName =
        application.student?.name || "Unknown";

    const studentEmail =
        application.student?.email || "Unknown";

    const schemeName =
        application.scheme?.name || "Unknown";

    const income =
        application.student?.annualIncome;

    const marks =
        application.student?.marks;

    const eligibility =
        application.eligibilityPassed
            ? "PASSED"
            : "FAILED";

    result.innerHTML = `
        <div class="status-card">

            <div class="status-grid">

                <div class="status-item">
                    <span>Application ID</span>
                    <strong>
                        #${escapeHtml(application.id)}
                    </strong>
                </div>

                <div class="status-item">
                    <span>Student</span>
                    <strong>
                        ${escapeHtml(studentName)}
                    </strong>
                </div>

                <div class="status-item">
                    <span>Email</span>
                    <strong>
                        ${escapeHtml(studentEmail)}
                    </strong>
                </div>

                <div class="status-item">
                    <span>Scholarship</span>
                    <strong>
                        ${escapeHtml(schemeName)}
                    </strong>
                </div>

                <div class="status-item">
                    <span>Annual Income</span>
                    <strong>
                        ₹${Number(income || 0)
        .toLocaleString("en-IN")}
                    </strong>
                </div>

                <div class="status-item">
                    <span>Marks</span>
                    <strong>
                        ${escapeHtml(marks)}
                    </strong>
                </div>

                <div class="status-item">
                    <span>Eligibility</span>
                    <strong>
                        ${eligibility}
                    </strong>
                </div>

                <div class="status-item">
                    <span>Application Status</span>
                    <strong>
                        ${escapeHtml(
        application.applicationStatus
    )}
                    </strong>
                </div>

                <div class="status-item">
                    <span>Disbursement</span>
                    <strong>
                        ${escapeHtml(
        application.disbursementStatus
    )}
                    </strong>
                </div>

                <div class="status-item">
                    <span>Applied At</span>
                    <strong>
                        ${escapeHtml(
        application.appliedAt
    )}
                    </strong>
                </div>

                <div class="status-item">
                    <span>Eligibility Message</span>
                    <strong>
                        ${escapeHtml(
        application.eligibilityMessage
    )}
                    </strong>
                </div>

            </div>

        </div>
    `;
}