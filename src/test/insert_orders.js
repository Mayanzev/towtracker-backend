import http from 'k6/http';
import { sleep } from 'k6';

export let options = {
  vus: 1,          // 1 пользователь
  iterations: 200, // 200 заказов
};

const token = '9adfbe27-ee62-4a1d-b06f-4cdfff2f9896';

export default function () {
  const id = Math.floor(Math.random() * 1_000_000);
  const fixedDate = "13.05.2025 21:36";

  const payload = JSON.stringify({
    tracks: [
      {
        id: id,
        time: "15",
        date: fixedDate,
        distance: "5",
        speed: "30",
        price: "500",
        firstCity: "CityA",
        secondCity: "CityB"
      }
    ],
    services: [
      {
        id: id + 1,
        name: "example",
        price: "1200",
        date: fixedDate
      },
      {
        id: id + 2,
        name: "example",
        price: "800",
        date: fixedDate
      }
    ]
  });

  const headers = {
    'Content-Type': 'application/json',
    'Bearer-Authorization': token,
  };

  const res = http.post('http://localhost:8080/order/insert', payload, { headers });

  sleep(0.1);
}
