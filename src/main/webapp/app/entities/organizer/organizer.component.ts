import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IOrganizer } from 'app/shared/model/organizer.model';
import { AccountService } from 'app/core/auth/account.service';
import { OrganizerService } from './organizer.service';

@Component({
  selector: 'jhi-organizer',
  templateUrl: './organizer.component.html'
})
export class OrganizerComponent implements OnInit, OnDestroy {
  organizers: IOrganizer[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected organizerService: OrganizerService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.organizerService
      .query()
      .pipe(
        filter((res: HttpResponse<IOrganizer[]>) => res.ok),
        map((res: HttpResponse<IOrganizer[]>) => res.body)
      )
      .subscribe((res: IOrganizer[]) => {
        this.organizers = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInOrganizers();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IOrganizer) {
    return item.id;
  }

  registerChangeInOrganizers() {
    this.eventSubscriber = this.eventManager.subscribe('organizerListModification', response => this.loadAll());
  }
}
