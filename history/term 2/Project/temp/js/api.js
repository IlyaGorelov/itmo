// API для работы с городами
class CitiesAPI {
    constructor() {
        // Тестовые данные
        this.cities = this.loadTestData();
        this.nextId = this.cities.length + 1;
    }

    loadTestData() {
        return [
            {
                id: 1,
                name: "Челябинск",
                shortDesc: "В годы войны Челябинск называли 'Танкоградом'.",
                fullDescription: "<h3>Танкоград</h3><p>В годы Великой Отечественной войны Челябинск стал крупнейшим центром танкостроения. На базе Челябинского тракторного завода был создан Кировский завод Наркомата танковой промышленности, который в народе получил название «Танкоград».</p><p>За годы войны завод произвел 18 тысяч танков и самоходных артиллерийских установок, 48 тысяч танковых дизелей. Каждый второй танк Т-34 имел двигатель, сделанный в Челябинске.</p><p>Труженики Танкограда работали по 16-18 часов в сутки, выполняя по 5-7 норм. За выдающиеся заслуги в годы войны город был награжден орденом Ленина, а в 2020 году удостоен звания «Город трудовой доблести».</p>",
                contribution: "Танки, двигатели, боеприпасы",
                categories: ["weapon", "tech"],
                image: "https://upload.wikimedia.org/wikipedia/commons/thumb/6/68/Kirov_Plant_in_Chelyabinsk_%28Tankograd%29.jpg/800px-Kirov_Plant_in_Chelyabinsk_%28Tankograd%29.jpg",
                lat: 55.1644,
                lng: 61.4368
            },
            {
                id: 2,
                name: "Нижний Тагил",
                shortDesc: "Главный производитель легендарных танков Т-34.",
                fullDescription: "<h3>Город танкостроителей</h3><p>Уральский танковый завод №183 в Нижнем Тагиле стал главным производителем легендарного танка Т-34. За годы войны завод выпустил более 30 000 машин.</p><p>Рабочие завода совершили настоящий трудовой подвиг, осваивая производство в нечеловеческих условиях. Многие из них жили прямо в цехах, чтобы не терять времени на дорогу.</p><p>В 2020 году Нижнему Тагилу присвоено звание «Город трудовой доблести».</p>",
                contribution: "Танки Т-34, бронекорпуса",
                categories: ["weapon", "tech"],
                image: "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1f/T-34-76_at_UMMC.jpg/800px-T-34-76_at_UMMC.jpg",
                lat: 57.9194,
                lng: 59.9653
            },
            {
                id: 3,
                name: "Новосибирск",
                shortDesc: "Крупнейший центр авиастроения и производства вооружений.",
                fullDescription: "<h3>Авиационная столица Сибири</h3><p>Завод им. Чкалова в Новосибирске выпускал истребители Як и Ла. Также город производил пулемёты, оптику и боеприпасы.</p><p>В Новосибирск были эвакуированы десятки заводов из западных регионов страны. Город стал крупнейшим промышленным центром Сибири.</p><p>За трудовой подвиг в годы войны Новосибирск удостоен звания «Город трудовой доблести».</p>",
                contribution: "Самолёты, пулемёты, оптика",
                categories: ["tech", "weapon"],
                image: "https://upload.wikimedia.org/wikipedia/commons/thumb/2/27/Chkalov_Aviation_Plant_Novosibirsk.jpg/800px-Chkalov_Aviation_Plant_Novosibirsk.jpg",
                lat: 55.0302,
                lng: 82.9204
            },
            {
                id: 4,
                name: "Омск",
                shortDesc: "Производство самолетов, «Катюш» и танков.",
                fullDescription: "<h3>Город-завод</h3><p>Омский авиационный завод выпускал истребители Як-9, бомбардировщики Ту-2. Также в Омске производили танки Т-34 и знаменитую реактивную систему залпового огня «Катюша».</p><p>Город принял эвакуированные заводы из Москвы, Ленинграда, Сталинграда. За годы войны промышленность Омска увеличилась в несколько раз.</p><p>Омск удостоен звания «Город трудовой доблести».</p>",
                contribution: "Самолёты, 'Катюши', танки",
                categories: ["tech", "weapon"],
                image: "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a3/Katyusha_launcher_during_Battle_of_Stalingand.jpg/800px-Katyusha_launcher_during_Battle_of_Stalingand.jpg",
                lat: 54.9885,
                lng: 73.3242
            },
            {
                id: 5,
                name: "Казань",
                shortDesc: "Производство пороха, самолетов и обмундирования.",
                fullDescription: "<h3>Пороховая столица</h3><p>Казань производила взрывчатку, порох, авиационные приборы. Авиазавод выпускал бомбардировщики Пе-2. Также здесь шили форму и выпускали медицинское оборудование.</p><p>Казанский пороховой завод работал круглосуточно, обеспечивая фронт боеприпасами. Город стал одним из ключевых центров оборонной промышленности.</p><p>В 2020 году Казани присвоено звание «Город трудовой доблести».</p>",
                contribution: "Порох, самолёты, обмундирование",
                categories: ["weapon", "uniform"],
                image: "https://upload.wikimedia.org/wikipedia/commons/thumb/d/dd/Kazan_Plant_Orgtekh.jpg/800px-Kazan_Plant_Orgtekh.jpg",
                lat: 55.7887,
                lng: 49.1221
            },
            {
                id: 6,
                name: "Красноярск",
                shortDesc: "Производство снарядов, мин и цветных металлов.",
                fullDescription: "<h3>Металлургический щит</h3><p>Красноярский машиностроительный завод выпускал снаряды и мины. Комбайновый завод перешёл на выпуск миномётов и авиабомб. Город стал центром производства цветных металлов для обороны.</p><p>Красноярский алюминиевый завод давал металл для авиации. Труженики тыла обеспечивали фронт необходимыми материалами.</p><p>Красноярск удостоен звания «Город трудовой доблести».</p>",
                contribution: "Снаряды, мины, металл",
                categories: ["weapon"],
                image: "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4e/Krasnoyarsk_Railway_Bridge.jpg/800px-Krasnoyarsk_Railway_Bridge.jpg",
                lat: 56.0153,
                lng: 92.8932
            },
            {
                id: 7,
                name: "Саратов",
                shortDesc: "Производство самолетов и продовольствия для фронта.",
                fullDescription: "<h3>Город на Волге</h3><p>Заводы Саратова выпускали истребители Як-1 и Як-3. Также город поставлял фронту знаменитую «Саратовскую гармонь» и продовольствие. Работал нефтеперерабатывающий завод.</p><p>Саратов стал важным логистическим центром, через который шло снабжение Сталинградского фронта.</p><p>Город удостоен звания «Город трудовой доблести».</p>",
                contribution: "Самолёты, продовольствие, ГСМ",
                categories: ["tech", "food"],
                image: "https://upload.wikimedia.org/wikipedia/commons/thumb/0/07/Saratov_Opera_House.jpg/800px-Saratov_Opera_House.jpg",
                lat: 51.5924,
                lng: 46.0106
            },
            {
                id: 8,
                name: "Самара (Куйбышев)",
                shortDesc: "Запасная столица, производство штурмовиков Ил-2.",
                fullDescription: "<h3>Запасная столица</h3><p>В годы войны Куйбышев (ныне Самара) стал запасной столицей СССР. Здесь производили штурмовики Ил-2 («летающие танки»), миномёты и взрывчатку. Располагались дипломатические посольства.</p><p>За годы войны заводы Куйбышева выпустили тысячи самолетов, которые сыграли решающую роль в битвах на фронте.</p><p>Самаре присвоено звание «Город трудовой доблести».</p>",
                contribution: "Штурмовики Ил-2, миномёты",
                categories: ["tech", "weapon"],
                image: "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f5/Il-2_in_museum.jpg/800px-Il-2_in_museum.jpg",
                lat: 53.1959,
                lng: 50.1008
            },
            {
                id: 9,
                name: "Пермь (Молотов)",
                shortDesc: "Производство артиллерийских орудий и снарядов.",
                fullDescription: "<h3>Арсенал Победы</h3><p>Мотовилихинские заводы Перми производили артиллерийские орудия (знаменитую «сорокапятку»), миномёты и снаряды. Каждый третий снаряд был изготовлен в Перми.</p><p>Труженики тыла Перми ковали оружие Победы, работая в нечеловеческих условиях. Заводы работали круглосуточно, выполняя военные заказы.</p><p>Пермь удостоена звания «Город трудовой доблести».</p>",
                contribution: "Пушки, снаряды, миномёты",
                categories: ["weapon"],
                image: "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5f/Perm_Motovilikha_Plant.jpg/800px-Perm_Motovilikha_Plant.jpg",
                lat: 58.0103,
                lng: 56.2567
            },
            {
                id: 10,
                name: "Иваново",
                shortDesc: "Главный текстильный центр, шивший обмундирование для армии.",
                fullDescription: "<h3>Город невест - город солдатского обмундирования</h3><p>Иваново стал главным текстильным центром страны в годы войны. Город шил обмундирование для всей армии: шинели, гимнастёрки, бельё. Работал в три смены, одевая миллионы солдат.</p><p>Ивановские текстильщицы работали по 14-16 часов в день, обеспечивая армию всем необходимым. Каждый второй солдат был одет в форму, сделанную в Иваново.</p><p>В 2020 году Иваново присвоено звание «Город трудовой доблести».</p>",
                contribution: "Обмундирование, ткани",
                categories: ["uniform"],
                image: "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8c/Ivanovo_Textile_Workers%27_Club.jpg/800px-Ivanovo_Textile_Workers%27_Club.jpg",
                lat: 57.0003,
                lng: 40.9835
            }
        ];
    }

    async getAllCities() {
        return this.cities;
    }

    async getCityById(id) {
        return this.cities.find(city => city.id === parseInt(id));
    }

    async createCity(cityData) {
        const newCity = {
            id: this.nextId++,
            ...cityData,
            categories: cityData.categories || []
        };
        this.cities.push(newCity);
        return newCity;
    }

    async updateCity(id, cityData) {
        const index = this.cities.findIndex(city => city.id === parseInt(id));
        if (index !== -1) {
            this.cities[index] = {
                ...this.cities[index],
                ...cityData,
                id: parseInt(id)
            };
            return this.cities[index];
        }
        return null;
    }

    async deleteCity(id) {
        const index = this.cities.findIndex(city => city.id === parseInt(id));
        if (index !== -1) {
            this.cities.splice(index, 1);
            return true;
        }
        return false;
    }
}

const api = new CitiesAPI();