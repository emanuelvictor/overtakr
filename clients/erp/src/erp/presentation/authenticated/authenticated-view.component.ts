// @ts-ignore
import {Component, OnDestroy, OnInit} from '@angular/core';
import {User} from '../../domain/model/user';
// @ts-ignore
import {Subscription} from 'rxjs';
import {Router} from '@angular/router';
import {MatDialog} from '@angular/material/dialog';
import {AuthenticationService} from '../../../common/application/services/authentication/authentication.service';
import {UserRepository} from '../../domain/repositories/user.repository';

@Component({
  selector: 'authenticated-view',
  templateUrl: './authenticated-view.component.html',
  styleUrl: './authenticated-view.component.scss', standalone: false
})
export class AuthenticatedViewComponent implements OnInit, OnDestroy {
  /**
   *
   */
  public user: User | undefined;
  public routerSubscription: Subscription | undefined = undefined;
  public userSubscription: Subscription | undefined = undefined;

  /**
   *
   */
  public toolbar: any = {headline: 'Cadastros', subhead: ''};

  /**
   *
   * @param userRepository
   * @param dialog
   * @param router
   * @param authenticationService
   */
  constructor(private userRepository: UserRepository,
              private dialog: MatDialog, private router: Router,
              private authenticationService: AuthenticationService) {
  }

  /**
   *
   */
  ngOnInit() {
    this.getAuthenticatedUser();
  }

  /**
   *
   */
  public logout() {
    this.authenticationService.logout()
  }

  /**
   *
   */
  public getAuthenticatedUser() {
    this.user = this.authenticationService.user;
    this.user = this.mockUser();
  }

  private mockUser(): User {
    const mockedUser = new User();
    mockedUser.id = 1;
    mockedUser.name = 'Fulano da Silva';
    mockedUser.email = 'silva@fulano.com';
    return mockedUser
  }

  /**
   *
   */
  public openDialogChangePassword() {
    // this.dialog.open(UpdatePasswordDialogComponent, {
    //   width: '400px',
    //   height: 'auto',
    //   data: {user: this.user || null}
    // })
  }

  /**
   * Verifica se o usuário logado é ADMINISTRADOR e se está editando ele mesmo.
   */
  public itsMe(user: any): boolean {
    const authenticatedUser = this.user;
    return authenticatedUser && ((authenticatedUser as any).isRoot || (authenticatedUser as any).id === user.id)
  }

  /**
   *
   */
  ngOnDestroy() {
    if (this.userSubscription) this.userSubscription.unsubscribe();
    if (this.routerSubscription) this.routerSubscription.unsubscribe()
  }
}
