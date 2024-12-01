-- Добавление нового столбца uploaded с типом TIMESTAMP
ALTER TABLE files
    ADD COLUMN uploaded TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Обновление уже существующих записей, если необходимо установить текущую дату и время
UPDATE files
SET uploaded = CURRENT_TIMESTAMP
WHERE uploaded IS NULL;