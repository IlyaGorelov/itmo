let currentCategory = 'all';
let currentSearchTerm = '';

async function loadCities() {
    const cities = await api.getAllCities();
    renderCities(cities);
}

function renderCities(cities) {
    const container = document.getElementById('citiesContainer');
    
    // Фильтрация
    let filtered = cities.filter(city => {
        if (currentCategory === 'all') return true;
        return city.categories.includes(currentCategory);
    });
    
    if (currentSearchTerm.trim() !== '') {
        const term = currentSearchTerm.toLowerCase().trim();
        filtered = filtered.filter(city => 
            city.name.toLowerCase().includes(term) || 
            city.shortDesc.toLowerCase().includes(term) ||
            city.contribution.toLowerCase().includes(term)
        );
    }
    
    if (filtered.length === 0) {
        container.innerHTML = '<div class="no-results">❌ Ничего не найдено. Попробуйте изменить запрос.</div>';
        return;
    }
    
    const categoryNames = {
        weapon: 'Оружие',
        tech: 'Техника',
        food: 'Продовольствие',
        uniform: 'Обмундирование'
    };
    
    let html = '';
    filtered.forEach(city => {
        const categoryChips = city.categories.map(cat => 
            `<span class="category-chip">${categoryNames[cat] || cat}</span>`
        ).join('');
        
        html += `
            <div class="city-card" onclick="window.location.href='/city?id=${city.id}'">
                <div class="city-image" style="background-image: linear-gradient(0deg, rgba(0,0,0,0.3), rgba(0,0,0,0.1)), url('${city.image}');">
                    <span class="city-tag">1941-1945</span>
                </div>
                <div class="city-info">
                    <div class="city-name">${city.name}</div>
                    <div class="city-desc">${city.shortDesc.substring(0, 100)}${city.shortDesc.length > 100 ? '...' : ''}</div>
                    <div class="contribution-badge">
                        <i>⚙️</i> ${city.contribution}
                    </div>
                    <div class="city-category">
                        ${categoryChips}
                    </div>
                </div>
            </div>
        `;
    });
    
    container.innerHTML = html;
}

// Обработчики фильтров
const searchInput = document.getElementById('searchInput');
if (searchInput) {
    searchInput.addEventListener('input', (e) => {
        currentSearchTerm = e.target.value;
        loadCities();
    });
}

const filterButtons = document.querySelectorAll('.filter-btn');
filterButtons.forEach(btn => {
    btn.addEventListener('click', () => {
        filterButtons.forEach(b => b.classList.remove('active'));
        btn.classList.add('active');
        currentCategory = btn.getAttribute('data-category');
        loadCities();
    });
});

loadCities();