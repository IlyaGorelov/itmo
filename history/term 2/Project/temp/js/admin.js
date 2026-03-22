let quill;
let currentCityId = null;

// Проверка авторизации
if (!auth.requireAuth()) {
    // Перенаправление уже происходит в requireAuth
}

async function loadCitiesList() {
    const cities = await api.getAllCities();
    const container = document.getElementById('citiesList');
    
    let html = `
        <table class="cities-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Название</th>
                    <th>Категории</th>
                    <th>Действия</th>
                </tr>
            </thead>
            <tbody>
    `;
    
    cities.forEach(city => {
        const categories = city.categories.map(c => {
            const names = { weapon: 'Оружие', tech: 'Техника', food: 'Продовольствие', uniform: 'Обмундирование' };
            return names[c] || c;
        }).join(', ');
        
        html += `
            <tr>
                <td>${city.id}</td>
                <td><strong>${city.name}</strong></td>
                <td>${categories}</td>
                <td class="action-buttons">
                    <button class="btn-edit" onclick="editCity(${city.id})">Редактировать</button>
                    <button class="btn-delete" onclick="deleteCity(${city.id})">Удалить</button>
                </td>
            </tr>
        `;
    });
    
    html += '</tbody></table>';
    container.innerHTML = html;
}

function openModal(cityId = null) {
    currentCityId = cityId;
    const modal = document.getElementById('cityModal');
    const modalTitle = document.getElementById('modalTitle');
    
    if (cityId) {
        modalTitle.textContent = 'Редактировать город';
        loadCityToForm(cityId);
    } else {
        modalTitle.textContent = 'Добавить город';
        document.getElementById('cityForm').reset();
        if (quill) quill.setContents([]);
        document.getElementById('fullDescription').value = '';
    }
    
    modal.style.display = 'flex';
}

function closeModal() {
    document.getElementById('cityModal').style.display = 'none';
    currentCityId = null;
}

async function loadCityToForm(cityId) {
    const city = await api.getCityById(cityId);
    if (!city) return;
    
    document.getElementById('cityName').value = city.name;
    document.getElementById('cityShortDesc').value = city.shortDesc;
    document.getElementById('cityContribution').value = city.contribution;
    document.getElementById('cityImage').value = city.image;
    document.getElementById('cityLat').value = city.lat || '';
    document.getElementById('cityLng').value = city.lng || '';
    
    if (quill) {
        quill.root.innerHTML = city.fullDescription || city.shortDesc;
    }
    document.getElementById('fullDescription').value = city.fullDescription || city.shortDesc;
    
    document.querySelectorAll('input[type="checkbox"]').forEach(cb => {
        cb.checked = city.categories.includes(cb.value);
    });
}

async function saveCity() {
    const cityData = {
        name: document.getElementById('cityName').value,
        shortDesc: document.getElementById('cityShortDesc').value,
        fullDescription: quill ? quill.root.innerHTML : document.getElementById('fullDescription').value,
        contribution: document.getElementById('cityContribution').value,
        categories: Array.from(document.querySelectorAll('input[type="checkbox"]:checked')).map(cb => cb.value),
        image: document.getElementById('cityImage').value,
        lat: parseFloat(document.getElementById('cityLat').value),
        lng: parseFloat(document.getElementById('cityLng').value)
    };
    
    if (!cityData.name) {
        alert('Название города обязательно');
        return;
    }
    
    let result;
    if (currentCityId) {
        result = await api.updateCity(currentCityId, cityData);
    } else {
        result = await api.createCity(cityData);
    }
    
    if (result) {
        closeModal();
        loadCitiesList();
    } else {
        alert('Ошибка при сохранении');
    }
}

async function deleteCity(cityId) {
    if (confirm('Вы уверены, что хотите удалить этот город?')) {
        await api.deleteCity(cityId);
        loadCitiesList();
    }
}

function editCity(cityId) {
    openModal(cityId);
}

// Инициализация Quill
if (document.getElementById('fullDescriptionEditor')) {
    quill = new Quill('#fullDescriptionEditor', {
        theme: 'snow',
        modules: {
            toolbar: [
                [{ 'header': [1, 2, 3, false] }],
                ['bold', 'italic', 'underline'],
                ['link', 'image'],
                [{ 'list': 'ordered'}, { 'list': 'bullet' }],
                ['clean']
            ]
        }
    });
    
    quill.on('text-change', () => {
        document.getElementById('fullDescription').value = quill.root.innerHTML;
    });
}

// Обработчики событий
document.getElementById('addCityBtn')?.addEventListener('click', () => openModal());
document.querySelector('.close-modal')?.addEventListener('click', closeModal);
document.getElementById('cancelBtn')?.addEventListener('click', closeModal);
document.getElementById('cityForm')?.addEventListener('submit', (e) => {
    e.preventDefault();
    saveCity();
});

// Закрытие модалки по клику вне
window.addEventListener('click', (e) => {
    const modal = document.getElementById('cityModal');
    if (e.target === modal) {
        closeModal();
    }
});

// Загрузка списка городов
loadCitiesList();