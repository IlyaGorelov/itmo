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
			v.ОЦЕНКА not in ('2', '3', '4', 'незач', 'неявка')
			and o.КОРОТКОЕ_ИМЯ = 'КТиУ'
		group by
			u.ИД
	);