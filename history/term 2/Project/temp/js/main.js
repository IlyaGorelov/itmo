// Загрузка статистики для главной страницы
async function loadStats() {
    const cities = await api.getAllCities();
    const citiesCountEl = document.getElementById('citiesCount');
    if (citiesCountEl) {
        citiesCountEl.textContent = cities.length;
    }
}

// Инициализация
if (document.getElementById('citiesCount')) {
    loadStats();
}