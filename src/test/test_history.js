// cd C:\Users\mayonnaise\IdeaProjects\towtracker-backend\src\test
// k6 run test_history.js

import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
  vus: 100,
  duration: '1m',
};

const token = '9adfbe27-ee62-4a1d-b06f-4cdfff2f9896';

export default function () {
  const headers = {
    'Bearer-Authorization': token,
  };

  const res = http.get('http://localhost:8080/order/get', { headers });

  check(res, {
    'Статус 200': (r) => r.status === 200,
  });

  sleep(1);
}
