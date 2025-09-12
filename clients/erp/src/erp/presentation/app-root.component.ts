import { Component } from "@angular/core";
import { AuthenticationService } from "../../common/application/services/authentication/authentication.service";

// @ts-ignore
@Component({
  "selector": 'app-root',
  template: `
   <router-outlet/>
  `, standalone: false
})
export class AppRootComponent {

  constructor(private authenticationService: AuthenticationService) {

  }

  logout() {
    this.authenticationService.logout();
  }
}
