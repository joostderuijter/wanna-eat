import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({providedIn: 'root'})
export class OrderService {
  constructor(private http: HttpClient) { }

  readonly ROOT_URL = 'http://localhost:4200';

  postOrder() {
    return this.http.get(this.ROOT_URL + '/api/order');
  }
}
