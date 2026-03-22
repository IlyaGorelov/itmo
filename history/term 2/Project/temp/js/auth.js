// Простая авторизация
class Auth {
    constructor() {
        this.isAuthenticated = false;
        this.credentials = {
            username: 'admin',
            password: 'admin123'
        };
        this.checkAuth();
    }

    login(username, password) {
        if (username === this.credentials.username && password === this.credentials.password) {
            this.isAuthenticated = true;
            localStorage.setItem('auth', 'true');
            return true;
        }
        return false;
    }

    logout() {
        this.isAuthenticated = false;
        localStorage.removeItem('auth');
        window.location.href = '/';
    }

    checkAuth() {
        this.isAuthenticated = localStorage.getItem('auth') === 'true';
        this.updateUI();
        return this.isAuthenticated;
    }

    updateUI() {
        const adminLink = document.getElementById('adminLink');
        const authLink = document.getElementById('authLink');
        
        if (adminLink) {
            adminLink.style.display = this.isAuthenticated ? 'block' : 'none';
        }
        
        if (authLink) {
            if (this.isAuthenticated) {
                authLink.textContent = 'Выйти';
                authLink.onclick = (e) => {
                    e.preventDefault();
                    this.logout();
                };
            } else {
                authLink.textContent = 'Войти';
                authLink.onclick = (e) => {
                    e.preventDefault();
                    window.location.href = '/login';
                };
            }
        }
    }

    requireAuth() {
        if (!this.checkAuth()) {
            window.location.href = '/login';
            return false;
        }
        return true;
    }
}

const auth = new Auth();

// Обработка формы входа
if (document.getElementById('loginForm')) {
    document.getElementById('loginForm').addEventListener('submit', (e) => {
        e.preventDefault();
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
        
        if (auth.login(username, password)) {
            const redirect = new URLSearchParams(window.location.search).get('redirect') || '/admin';
            window.location.href = redirect;
        } else {
            const errorEl = document.getElementById('loginError');
            errorEl.style.display = 'block';
        }
    });
}