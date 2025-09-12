import {Component} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthenticatedViewComponent} from '../../../authenticated-view.component';
import {Product} from '../../../../../domain/model/product';
import {MessageService} from '../../../../../../common/application/services/message.service';
import {ProductRepository} from '../../../../../domain/repositories/product.repository';

// @ts-ignore
@Component({
  selector: 'insert-products',
  templateUrl: 'insert-product.component.html', standalone: false
})
export class InsertProductComponent {

  /**
   *
   */
  product: Product = new Product();

  /**
   *
   * @param router
   * @param homeView
   * @param activatedRoute
   * @param messageService
   * @param productRepository
   */
  constructor(private router: Router,
              private messageService: MessageService,
              private homeView: AuthenticatedViewComponent,
              private productRepository: ProductRepository) {
    homeView.toolbar.subhead = 'Produtos / Adicionar';
  }

  public save(form: { invalid: any; }) {

    if (form.invalid) {
      this.messageService.toastWarning();
      return;
    }

    this.productRepository.save(this.product)
      .then(() => {
        this.router.navigate(['stocks/products']);
        this.messageService.toastSuccess();
      });
  }

}
