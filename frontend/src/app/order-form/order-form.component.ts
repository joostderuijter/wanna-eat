import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {OrderService} from '../service/order.service';
import {Order} from '../model/order';

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
  successUrl: string;

  constructor(
    private formBuilder: FormBuilder,
    private orderService: OrderService) {
  }

  ngOnInit(): void {
    this.orderForm = this.formBuilder.group({
      fullName: ['', Validators.required],
      street: ['', Validators.required],
      houseNumber: ['', Validators.required],
      city: ['', Validators.required],
      postalCode: ['', Validators.required],
      email: ['', Validators.required],
      phoneNumber: ['', Validators.required],
      amountToSpend: ['', Validators.required],
      bank: ['', Validators.required]
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
    this.orderService.postOrder(new Order(orderData)).subscribe(result => {
      this.successUrl = result.orderUrl;
      this.showSpinner = false;
      this.orderPlaced = true;
    },
      error => {
      this.showSpinner = false;
      this.orderForm.enable();
      this.showError = true;
    });
  }
}
