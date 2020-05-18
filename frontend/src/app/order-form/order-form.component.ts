import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {OrderService} from '../service/order.service';

@Component({
  selector: 'app-order-form',
  templateUrl: './order-form.component.html',
  styleUrls: ['./order-form.component.css']
})
export class OrderFormComponent implements OnInit {

  orderForm;
  showSpinner = false;
  orderPlaced = false;
  showError = false;

  constructor(
    private formBuilder: FormBuilder,
    private orderService: OrderService) {
  }

  ngOnInit(): void {
    this.orderForm = this.formBuilder.group({
      fullName: '',
      street: '',
      houseNumber: '',
      city: '',
      postalCode: '',
      email: '',
      phoneNumber: '',
      amountToSpend: '',
      bank: ''
    });

    this.orderForm.valueChanges.subscribe(val => {
      this.showError = false;
    });
  }

  resetForm() {
    this.orderPlaced = false;
    this.orderForm.enable();
  }

  onSubmit(orderData) {
    this.showSpinner = true;
    this.orderForm.disable();
    console.log(orderData);
    this.orderService.postOrder().subscribe(result => {
      console.log(result);
      this.showSpinner = false;
      this.orderPlaced = true;
    },
      error => {
      console.log(error);
      this.showSpinner = false;
      this.orderForm.enable();
      this.showError = true;
    });
  }
}
