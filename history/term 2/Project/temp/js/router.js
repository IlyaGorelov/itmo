// Маршрутизатор для хэш-навигации
class Router {
    constructor() {
        this.handleRoute = this.handleRoute.bind(this);
        window.addEventListener('hashchange', this.handleRoute);
        this.handleRoute();
    }

    handleRoute() {
        let hash = window.location.hash.slice(1) || '/';
        
        // Убираем query параметры для определения страницы
        let page = hash;
        if (hash.includes('?')) {
            page = hash.split('?')[0];
        }
        
        // Если это страница города, загружаем city.html с параметрами
        if (page === '/city.html') {
            this.loadPage('city.html', hash);
        } 
        // Если это главная страница
        else if (page === '/' || page === '') {
            this.loadPage('index.html');
        }
        // Обычные страницы
        else {
            this.loadPage(page);
        }
        
        this.updateActiveLink(page);
    }
    
    loadPage(url, hash = null) {
        // Сохраняем текущий хэш для city.html
        if (hash && url === 'city.html') {
            sessionStorage.setItem('pendingHash', hash);
        }
        window.location.href = url;
    }
    
    updateActiveLink(currentPath) {
        // Эта функция будет вызываться после загрузки страницы
        setTimeout(() => {
            const links = document.querySelectorAll('.nav-link');
            links.forEach(link => {
                const href = link.getAttribute('href');
                if (href === `#${currentPath}` || (currentPath === '/' && href === '#/')) {
                    link.style.color = '#b22222';
                    link.style.fontWeight = 'bold';
                } else {
                    link.style.color = '#ddd';
                    link.style.fontWeight = 'normal';
                }
            });
        }, 100);
    }
    
    navigateTo(path) {
        window.location.hash = path;
    }
}

const router = new Router();