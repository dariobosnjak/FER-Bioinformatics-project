import { matchPairs } from "./matchPairs";
import { EventType, Event, Pair } from "./interfaces";
import * as _ from "lodash";
import { eventComparator } from "./util";
import FenwickTree from "./fenwickTree";
import * as wu from "wu";

export function lcskPlusPlus(seqA: string, seqB: string, k: number): number {
  const dp = new Map<Pair, number>();
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
      const val = k + maxColDp.query(event.pair.j);
      dp.set(event.pair, val);
    } else {
      const g = findG(event, events);
      if (g) {
        const val = Math.max(
          dp.get(event.pair) || 0,
          (dp.get(g.pair) || 0) + 1
        );
        dp.set(event.pair, val);
      }
      maxColDp.update(
        event.pair.j + k,
        Math.max(maxColDp.get(event.pair.j + k) || 0, dp.get(event.pair) || 0)
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
        return null;
    }
  }
  return g;
}
