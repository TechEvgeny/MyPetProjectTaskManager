package com.evgenjoy.myPetProject1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Сохранить значение в Redis с TTL
     * @param key - ключ
     * @param value - значение
     * @param ttlMinutes - время жизни в минутах
     */
    public void set(String key, Object value, long ttlMinutes) {
        try {
            redisTemplate.opsForValue().set(key, value, ttlMinutes, TimeUnit.MINUTES);
            log.debug("Сохранено в Redis: ключ={}, ttl={} мин", key, ttlMinutes);
        }
        catch (Exception e) {
            log.error("Ошибка сохранения в redis: {}", e.getMessage());
        }
    }

    /**
     * Получить значение из Redis
     * @param key - ключ
     * @return значение или null если не найдено
     */
    public Object get(String key) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            log.debug("Получено значение из Redis: ключ={}, найдено={}", key, value != null);
            return value;
        }
        catch (Exception e) {
            log.error("Ошибка чтения из Redis: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Удалить значение из Redis
     * @param key - ключ
     */
    public void delete(String key) {
        try {
            redisTemplate.delete(key);
            log.debug("Удалено из Redis: key={}", key);
        }
        catch (Exception e) {
            log.error("Не найдено в Redis: {}", e.getMessage());
        }
    }

    /**
     * Проверить существование ключа
     * @param key - ключ
     * @return true если ключ существует
     */
    public boolean exists(String key) {
        try {
            Boolean exists  = redisTemplate.hasKey(key);
            log.debug("Найдено в Redis: key={}", key, exists);
            return exists ;
        }
        catch (Exception e) {
            log.error("Не найдено в Redis: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Установить время жизни ключа
     * @param key - ключ
     * @param ttlMinutes - время жизни в минутах
     */
    public void expire(String key, long ttlMinutes) {
        try {
            redisTemplate.expire(key, ttlMinutes, TimeUnit.MINUTES);
            log.debug("Установлен TTL для ключа: {} -> {} мин", key, ttlMinutes);
        }
        catch (Exception e) {
            log.error("Ошибка установки TTL: {}", e.getMessage());
        }
    }

    /**
     * Получить оставшееся время жизни ключа
     * @param key - ключ
     * @return время в минутах или -2 если не существует
     */
    public long getTtl(String key) {
        try {
            Long ttl = redisTemplate.getExpire(key, TimeUnit.MINUTES);
            return ttl != null ? ttl : -2;
        }
        catch (Exception e) {
            log.error("Ошибка получения TTL: {}", e.getMessage());
            return -2;
        }
    }

    /**
     * Очистить все данные в Redis
     */
    public void flushAll() {
        try {
            redisTemplate.getConnectionFactory().getConnection().flushAll();
            log.warn("Очищен весь кэш Redis");
        } catch (Exception e) {
            log.error("Ошибка очистки Redis: {}", e.getMessage());
        }
    }
}
