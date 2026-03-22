async function loadCity() {
    const urlParams = new URLSearchParams(window.location.search);
    const cityId = urlParams.get('id');
    
    if (!cityId) {
        window.location.href = '/search';
        return;
    }
    
    const city = await api.getCityById(cityId);
    
    if (!city) {
        document.getElementById('cityContent').innerHTML = '<div class="container"><div class="no-results">Город не найден</div></div>';
        return;
    }
    
    const categoryNames = {
        weapon: 'Оружие',
        tech: 'Техника',
        food: 'Продовольствие',
        uniform: 'Обмундирование'
    };
    
    const categoryChips = city.categories.map(cat => 
        `<span class="category-chip">${categoryNames[cat] || cat}</span>`
    ).join('');
    
    const html = `
        <div class="city-detail">
            <div class="city-detail-header" style="background-image: linear-gradient(to top, rgba(0,0,0,0.8), rgba(0,0,0,0.3)), url('${city.image}');">
                <h1>${city.name}</h1>
            </div>
            <div class="city-detail-content">
                <div class="city-category" style="margin-bottom: 20px;">
                    ${categoryChips}
                </div>
                <div class="contribution">
                    <strong>Вклад в Победу:</strong> ${city.contribution}
                </div>
                <h2>О городе</h2>
                <div class="full-description">
                    ${city.fullDescription || city.shortDesc}
                </div>
            </div>
        </div>
    `;
    
    document.getElementById('cityContent').innerHTML = html;
    document.title = `${city.name} - Город трудовой доблести`;
}

loadCity();