// @ts-ignore
import {Component, OnInit} from '@angular/core';
import {CrudViewComponent} from '../../../../../../../common/presentation/crud/crud-view.component';
import {MAT_FORM_FIELD_DEFAULT_OPTIONS, MatFormFieldDefaultOptions} from '@angular/material/form-field';
// @ts-ignore
import {Validators} from '@angular/forms';


const appearance: MatFormFieldDefaultOptions = {
  appearance: 'outline'
};

@Component({
  selector: 'product-form',
  templateUrl: 'product-form.component.html',
  standalone: false,
  providers: [
    {
      provide: MAT_FORM_FIELD_DEFAULT_OPTIONS,
      useValue: appearance
    }
  ]
})
export class ProductFormComponent extends CrudViewComponent implements OnInit {

  ngOnInit() {
    this.form = this.fb.group({
      name: ['name', [Validators.required]]
    });
  }
}
