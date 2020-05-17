import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';

@Injectable({providedIn: 'root'})
export class OrderService {
  constructor(private http: HttpClient) { }

  readonly ROOT_URL = 'localhost:8080';

  postOrder() {
    return this.http.get(this.ROOT_URL + '/api/order');
  }
}

