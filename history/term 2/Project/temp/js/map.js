let map;

async function initMap() {
    const cities = await api.getAllCities();
    
    map = L.map('map').setView([55.0, 60.0], 4);
    
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors'
    }).addTo(map);
    
    cities.forEach(city => {
        if (city.lat && city.lng) {
            const marker = L.marker([city.lat, city.lng]).addTo(map);
            marker.bindPopup(`
                <b>${city.name}</b><br>
                ${city.shortDesc}<br>
                <a href="/city?id=${city.id}">Подробнее</a>
            `);
        }
    });
}

if (document.getElementById('map')) {
    initMap();
}