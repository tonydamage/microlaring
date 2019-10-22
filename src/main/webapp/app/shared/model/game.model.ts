import { IInstance } from 'app/shared/model/instance.model';
import { IOrganizer } from 'app/shared/model/organizer.model';
import { IRuleset } from 'app/shared/model/ruleset.model';

export interface IGame {
  id?: number;
  instances?: IInstance[];
  organizers?: IOrganizer[];
  rulesets?: IRuleset[];
}

export class Game implements IGame {
  constructor(public id?: number, public instances?: IInstance[], public organizers?: IOrganizer[], public rulesets?: IRuleset[]) {}
}
