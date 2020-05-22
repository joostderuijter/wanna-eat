import {HttpParams} from '@angular/common/http';
import {HttpModel} from './http-model';

export class Order implements HttpModel {
  fullName: string;
  street: string;
  houseNumber: string;
  city: string;
  postalCode: string;
  email: string;
  phoneNumber: string;
  amountToSpend: string;
  bank: string;

  public constructor(init?: Partial<Order>) {
    Object.assign(this, init);
  }

  public getAsHttpParams(): HttpParams {
    return new HttpParams()
      .set('fullName', `${this.fullName}`)
      .append('street', `${this.street}`)
      .append('houseNumber', `${this.houseNumber}`)
      .append('city', `${this.city}`)
      .append('postalCode', `${this.postalCode}`)
      .append('email', `${this.email}`)
      .append('phoneNumber', `${this.phoneNumber}`)
      .append('amountToSpend', `${this.amountToSpend}`)
      .append('bank', `${this.bank}`);
  }
}
