select
    u.ГРУППА,
    u.ИД,
    l.ФАМИЛИЯ,
    ИМЯ,
    ОТЧЕСТВО,
    СОСТОЯНИЕ
from
    Н_УЧЕНИКИ u
    join Н_ЛЮДИ l on l.ИД = u.ЧЛВК_ИД
where
    u.ПЛАН_ИД in (
        select
            p.ИД
        from
            Н_ПЛАНЫ p
            join Н_ФОРМЫ_ОБУЧЕНИЯ fo on fo.ИД = p.ФО_ИД
			join Н_НАПРАВЛЕНИЯ_СПЕЦИАЛ naprspec on naprspec.ИД = p.НАПС_ИД
            join Н_НАПР_СПЕЦ ns on ns.ИД = naprspec.НАПС_ИД
        where
             p.КУРС = 1
            and fo.НАИМЕНОВАНИЕ = 'Очная'
            and ns.КОД_НАПРСПЕЦ = '23020001'
    ) and u.НАЧАЛО= '2001-09-01';



	--
	select ns.КОД_НАПРСПЕЦ
        from
            Н_ПЛАНЫ p
            join Н_ФОРМЫ_ОБУЧЕНИЯ fo on fo.ИД = p.ФО_ИД
            join Н_НАПРАВЛЕНИЯ_СПЕЦИАЛ naprspec on naprspec.ИД = p.НАПС_ИД
            join Н_НАПР_СПЕЦ ns on ns.ИД = naprspec.НАПС_ИД
        where
            p.КУРС = 1
            and fo.НАИМЕНОВАНИЕ = 'Очная';
      --      and ns.КОД_НАПРСПЕЦ = '230101'


select
            p.ИД, p.УЧЕБНЫЙ_ГОД
        from
            Н_ПЛАНЫ p
            join Н_ФОРМЫ_ОБУЧЕНИЯ fo on fo.ИД = p.ФО_ИД
            join Н_НАПРАВЛЕНИЯ_СПЕЦИАЛ naprspec on naprspec.ИД = p.НАПС_ИД
            join Н_НАПР_СПЕЦ ns on ns.ИД = naprspec.НАПС_ИД
        where
            p.КУРС = 1
            and fo.НАИМЕНОВАНИЕ = 'Очная'
            and ns.КОД_НАПРСПЕЦ = '23020001';



select u.НАЧАЛО, fo.НАИМЕНОВАНИЕ, p.КУРС, p.ИД 
from Н_УЧЕНИКИ u  
join Н_ПЛАНЫ p on p.ИД=u.ПЛАН_ИД 
join Н_ФОРМЫ_ОБУЧЕНИЯ fo on fo.ИД = p.ФО_ИД 
where u.НАЧАЛО='2009-09-01' and p.КУРС=1 
and fo.НАИМЕНОВАНИЕ='Очная';


select
    count(*)
from
    (
        select
            u.ИД
        from
            Н_УЧЕНИКИ u
            join Н_ВЕДОМОСТИ v on v.ЧЛВК_ИД = u.ЧЛВК_ИД
            join Н_ПЛАНЫ p on u.ПЛАН_ИД = p.ИД
            join Н_ОТДЕЛЫ o on o.ИД = p.ОТД_ИД
        where
            v.ОЦЕНКА ='99'
            and o.КОРОТКОЕ_ИМЯ = 'КТиУ'
        group by
            u.ИД
    );
