import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
  vus: 100,        // 100 пользователей
  duration: '1m' // за 10 секунд
};

export default function () {
  const id = Math.floor(Math.random() * 1_000_000);
  const login = `user${id}@test.com`;
  const password = '123456';
  const newPassword = '654321';

  const headers = {
    'Content-Type': 'application/json'
  };

  // 1. Регистрация
  const registerPayload = JSON.stringify({
    login: login,
    password: password,
    username: `User${id}`
  });

  const registerRes = http.post('http://127.0.0.1:8080/register', registerPayload, { headers });

  const token = JSON.parse(registerRes.body).token;

  check(registerRes, {
    'Регистрация 200': (r) => r.status === 200,
    'Токен получен': (r) => token !== undefined,
  });

  // 2. Смена пароля
  const passwordHeaders = {
    'Content-Type': 'application/json',
    'Bearer-Authorization': token
  };

  const passwordPayload = JSON.stringify({
    login: login,
    password: password,
    newPassword: newPassword
  });

  const changeRes = http.post('http://127.0.0.1:8080/user/update/password', passwordPayload, { headers: passwordHeaders });

  check(changeRes, {
    'Пароль обновлён (200)': (r) => r.status === 200,
  });

  sleep(1);
}
