import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IInstance } from 'app/shared/model/instance.model';
import { AccountService } from 'app/core/auth/account.service';
import { InstanceService } from './instance.service';

@Component({
  selector: 'jhi-instance',
  templateUrl: './instance.component.html'
})
export class InstanceComponent implements OnInit, OnDestroy {
  instances: IInstance[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected instanceService: InstanceService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.instanceService
      .query()
      .pipe(
        filter((res: HttpResponse<IInstance[]>) => res.ok),
        map((res: HttpResponse<IInstance[]>) => res.body)
      )
      .subscribe((res: IInstance[]) => {
        this.instances = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInInstances();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IInstance) {
    return item.id;
  }

  registerChangeInInstances() {
    this.eventSubscriber = this.eventManager.subscribe('instanceListModification', response => this.loadAll());
  }
}
