import {CanActivate, Router} from "@angular/router";
import {HasPermissionDirective} from "./has-permission";
import {AuthenticationService} from '../../application/services/authentication/authentication.service';
// @ts-ignore
import {Observable} from 'rxjs';

export abstract class DefaultCanActivate implements CanActivate {

  // Rota de fallback. O usuário será redirecionado para essa rota caso não tenha permissão de acesso a página.
  public fallbackRoute: string = 'selecoes';

  // Authorities disponíveis para acesso á página.
  // O usuário deve ter  pelo menos uma delas.
  public permissions: string[] = ['root'];

  /**
   *
   * @param authenticationService
   * @param router
   */
  constructor(public authenticationService: AuthenticationService, private router: Router) {
  }

  /**
   *
   */
  canActivate(): Observable<any> {

    // @ts-ignore
    return this.authenticationService.getObservedLoggedUser().map((user: { authorities?: any; }) => {
      for (let i = 0; i < this.permissions.length; i++) {
        const permission = this.permissions[i];

        if (HasPermissionDirective.checkPermission(user, [permission]))
          return true;
        else if (i === (this.permissions.length - 1)) {
          this.router.navigate([this.fallbackRoute]).then(value => console.log(value));
          return false
        }
      }
    })
  }

}
