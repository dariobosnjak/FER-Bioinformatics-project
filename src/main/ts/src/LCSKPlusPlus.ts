import { matchPairs } from "./matchPairs";
import { EventType, Event } from "./interfaces";
import * as _ from "lodash";
import { eventComparator } from "./util";
import * as wu from "wu";
import FenwickTree from "./fenwickTree";

export function lcskPlusPlus(seqA: string, seqB: string, k: number): number {
  const dp = new Map<string, number>();
  const maxColDp = new FenwickTree(seqB.length);

  const mPairs = matchPairs(seqA, seqB, k);
  const events: Array<Event> = _.flatMap(mPairs, matchPair => [
    {
      pair: matchPair.start,
      type: EventType.Start
    },
    {
      pair: matchPair.end,
      type: EventType.End
    }
  ]);
  events.sort(eventComparator);

  events.forEach(event => {
    if (event.type === EventType.Start) {
      dp.set(s(event.pair), k + (maxColDp.query(event.pair.j) || 0));
    } else {
      const p: Event = {
        pair: { i: event.pair.i - k, j: event.pair.j - k },
        type: event.type
      };
      const g = findG(p, events);
      if (g) {
        dp.set(s(p.pair), (dp.get(s(g.pair)) || 0) + 1);
      }
      maxColDp.update(
        p.pair.j + k,
        Math.max(
          maxColDp.get(event.pair.j + k) || 0,
          dp.get(s(event.pair)) || 0
        )
      );
    }
  });

  if (dp.size > 0) {
    return wu(dp.values()).reduce((max, val) => Math.max(max, val));
  }
  return 0;
}

function findG(p: Event, events: Array<Event>): Event {
  const g: Event = {
    pair: { i: p.pair.i - 1, j: p.pair.j - 1 },
    type: EventType.Start
  };

  let left = 0;
  let right = events.length - 1;

  while (left <= right) {
    const pivot = Math.floor((right + left) / 2);
    const compare = eventComparator(events[pivot], g);
    switch (compare) {
      case -1:
        left = pivot + 1;
        break;
      case 1:
        right = pivot - 1;
        break;
      case 0:
        return g;
    }
  }
  return null;
}

function s(obj: any) {
  return JSON.stringify(obj);
}
