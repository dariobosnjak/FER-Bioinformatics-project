export interface Pair {
  i: number;
  j: number;
}

export interface MatchPair {
  start: Pair;
  end: Pair;
}

export enum EventType {
  Start,
  End
}

export interface Event {
  pair: Pair;
  type: EventType;
}
