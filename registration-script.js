// Registration form handling
async function register(event) {
    event.preventDefault();
    
    const username = document.getElementById("regUsername").value.trim();
    const email = document.getElementById("regEmail").value.trim();
    const password = document.getElementById("regPassword").value.trim();
    const passwordConfirm = document.getElementById("regPasswordConfirm").value.trim();
    
    // Basic validation
    if (!username || !password) {
        showNotification("Username and password are required", "error");
        return;
    }
    
    if (password !== passwordConfirm) {
        showNotification("Passwords do not match", "error");
        return;
    }
    
    try {
        const res = await fetch(`${AUTH_API}/register`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, email, password })
        });
        
        if (!res.ok) throw new Error(await res.text());
        const data = await res.json();
        const token = data.token;
        
        sessionStorage.setItem("authToken", token);
        sessionStorage.setItem("loggedIn", "true");
        sessionStorage.setItem("username", username);
        
        showNotification("✅ Registration successful! Redirecting...", "success");
        
        setTimeout(() => {
            window.location.href = "index.html";
        }, 1000);
    } catch (err) {
        showNotification("Registration failed: " + err.message, "error");
    }
}

// Add event listener when DOM is loaded
document.addEventListener("DOMContentLoaded", () => {
    const registerForm = document.getElementById("registerForm");
    if (registerForm) {
        registerForm.addEventListener("submit", register);
    }
});