import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Order} from '../model/order';
import {Observable} from 'rxjs';
import {OrderResponse} from '../rest/order-response';

@Injectable({providedIn: 'root'})
export class OrderService {
  constructor(private http: HttpClient) { }

  readonly ROOT_URL = 'http://localhost:4200';

  postOrder(order: Order): Observable<OrderResponse> {
    return this.http.get<OrderResponse>(this.ROOT_URL + '/api/order', {params: order.getAsHttpParams()});
  }
}
