import {Component} from '@angular/core';
import {Router} from "@angular/router";
import {DefaultCanActivate} from '../../../../common/presentation/has-permission/default-can-activate';
import {AuthenticationService} from '../../../../common/application/services/authentication/authentication.service';

@Component({
  selector: 'inventory-view',
  templateUrl: './inventory-view.component.html',
  styleUrl: './inventory-view.component.scss', standalone: false
})
export class InventoryViewComponent extends DefaultCanActivate {

  /**
   *
   * @param authenticationService
   * @param router
   */
  constructor(authenticationService: AuthenticationService, router: Router) {

    super(authenticationService, router);

    this.fallbackRoute = 'minha-conta';

    this.permissions = ['root']

  }
}
