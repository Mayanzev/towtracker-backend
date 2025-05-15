import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
  vus: 100,
  duration: '10s',
};

export default function () {
  const id = Math.floor(Math.random() * 1_000_000);

  const payload = JSON.stringify({
    login: `user${id}@test.com`,
    username: `User${id}`,
    password: "123456"
  });

  const headers = {
    'Content-Type': 'application/json',
  };

  const res = http.post('http://127.0.0.1:8080/register', payload, { headers });

  check(res, {
    'Статус 200': (r) => r.status === 200,
  });

  sleep(1);
}
