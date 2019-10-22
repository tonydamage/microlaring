import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IRuleset } from 'app/shared/model/ruleset.model';
import { AccountService } from 'app/core/auth/account.service';
import { RulesetService } from './ruleset.service';

@Component({
  selector: 'jhi-ruleset',
  templateUrl: './ruleset.component.html'
})
export class RulesetComponent implements OnInit, OnDestroy {
  rulesets: IRuleset[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected rulesetService: RulesetService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.rulesetService
      .query()
      .pipe(
        filter((res: HttpResponse<IRuleset[]>) => res.ok),
        map((res: HttpResponse<IRuleset[]>) => res.body)
      )
      .subscribe((res: IRuleset[]) => {
        this.rulesets = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInRulesets();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IRuleset) {
    return item.id;
  }

  registerChangeInRulesets() {
    this.eventSubscriber = this.eventManager.subscribe('rulesetListModification', response => this.loadAll());
  }
}
