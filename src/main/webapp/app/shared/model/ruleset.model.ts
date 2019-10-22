import { IGame } from 'app/shared/model/game.model';
import { IOrganizer } from 'app/shared/model/organizer.model';

export interface IRuleset {
  id?: number;
  games?: IGame[];
  organizers?: IOrganizer[];
}

export class Ruleset implements IRuleset {
  constructor(public id?: number, public games?: IGame[], public organizers?: IOrganizer[]) {}
}
