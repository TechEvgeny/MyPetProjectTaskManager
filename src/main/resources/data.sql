INSERT INTO tasks (title, description, status, priority, due_date, is_deleted, created_at, updated_at, version)
VALUES
    ('Изучить Spring Boot', 'Пройти курс по Spring Boot', 'CREATE', 'HIGH',
     '2026-12-31 23:59:59', false, NOW(), NOW(), 1),
    ('Сделать домашку', 'Выполнить домашнее задание по Java', 'CREATE', 'MEDIUM',
     '2026-11-30 18:00:00', false, NOW(), NOW(), 1),
    ('Подготовить презентацию', 'Создать презентацию для митапа', 'CREATE', 'LOW',
     '2026-12-15 12:00:00', false, NOW(), NOW(), 1),
    ('Починить баг', 'Исправить критический баг в продакшене', 'CREATE', 'HIGH',
     '2026-11-25 10:00:00', false, NOW(), NOW(), 1),
    ('Написать тесты', 'Добавить unit-тесты для сервиса задач', 'CREATE', 'MEDIUM',
     '2026-11-20 17:00:00', false, NOW(), NOW(), 1);