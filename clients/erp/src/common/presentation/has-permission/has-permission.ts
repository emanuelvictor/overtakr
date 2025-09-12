// @ts-ignore
import {Directive, Input, OnInit, TemplateRef, ViewContainerRef} from "@angular/core";
import {AuthenticationService} from '../../application/services/authentication/authentication.service';

@Directive({selector: '[hasPermission]'})
export class HasPermissionDirective implements OnInit {

  /**
   *
   */
  private operation = {};

  /**
   *
   */
  private currentUser = {};

  /**
   *
   */
  private authorities: string[] = [];

  /**
   *
   * @param templateRef
   * @param viewContainer
   * @param authenticationService
   */
  constructor(private templateRef: TemplateRef<any>,
              private viewContainer: ViewContainerRef,
              private authenticationService: AuthenticationService) {
  }

  /**
   *
   */
  ngOnInit() {
    if (this.authenticationService.user && this.authenticationService.user.authorities && this.authenticationService.user.authorities.length) {
      this.currentUser = this.authenticationService.user;
      this.updateView()
    }
  }

  @Input()
  // @ts-ignore
  set hasPermission(val: string[]) {
    this.authorities = val;
    this.updateView()
  }

  @Input()
  // @ts-ignore
  set hasPermissionOperation(operation: string) {
    this.operation = operation
  }

  /**
   *
   */
  private updateView() {
    if (HasPermissionDirective.checkPermission(this.currentUser, this.authorities)) {
      this.viewContainer.createEmbeddedView(this.templateRef)
    } else {
      this.viewContainer.clear()
    }
  }

  /**
   *
   */
  public static checkPermission(currentUser: { authorities?: any; }, authorities: never[] | string[]) {

    let hasPermission = false;
    if (currentUser && currentUser.authorities) {
      for (const checkPermission of authorities) {
        const permissionFound = currentUser.authorities.find((authority: { toUpperCase: () => string; }) =>
          authority.toUpperCase() === checkPermission.toUpperCase());
        if (permissionFound)
          hasPermission = true
      }
    }

    return hasPermission
  }
}
