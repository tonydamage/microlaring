import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Ruleset } from 'app/shared/model/ruleset.model';
import { RulesetService } from './ruleset.service';
import { RulesetComponent } from './ruleset.component';
import { RulesetDetailComponent } from './ruleset-detail.component';
import { RulesetUpdateComponent } from './ruleset-update.component';
import { RulesetDeletePopupComponent } from './ruleset-delete-dialog.component';
import { IRuleset } from 'app/shared/model/ruleset.model';

@Injectable({ providedIn: 'root' })
export class RulesetResolve implements Resolve<IRuleset> {
  constructor(private service: RulesetService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IRuleset> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Ruleset>) => response.ok),
        map((ruleset: HttpResponse<Ruleset>) => ruleset.body)
      );
    }
    return of(new Ruleset());
  }
}

export const rulesetRoute: Routes = [
  {
    path: '',
    component: RulesetComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'microlarpingApp.ruleset.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: RulesetDetailComponent,
    resolve: {
      ruleset: RulesetResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'microlarpingApp.ruleset.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: RulesetUpdateComponent,
    resolve: {
      ruleset: RulesetResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'microlarpingApp.ruleset.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: RulesetUpdateComponent,
    resolve: {
      ruleset: RulesetResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'microlarpingApp.ruleset.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const rulesetPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: RulesetDeletePopupComponent,
    resolve: {
      ruleset: RulesetResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'microlarpingApp.ruleset.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
