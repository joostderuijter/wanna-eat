import {HttpParams} from '@angular/common/http';

export interface HttpModel {
  getAsHttpParams(): HttpParams;
}
