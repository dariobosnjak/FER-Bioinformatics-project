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
        type: EventType.Start
      };
      const g: Event = {
        pair: { i: p.pair.i - 1, j: p.pair.j - 1 },
        type: EventType.Start
      };
      if (dp.has(s(g.pair))) {
        dp.set(s(p.pair), Math.max(dp.get(s(p.pair)), dp.get(s(g.pair)) + 1));
      }
      maxColDp.update(event.pair.j, dp.get(s(p.pair)));
    }
  });

  if (dp.size > 0) {
    return wu(dp.values()).reduce((max, val) => Math.max(max, val));
  }
  return 0;
}

function s(obj: any) {
  return JSON.stringify(obj);
}
