import { matchPairs } from "./matchPairs";
import { EventType, Event, Pair } from "./interfaces";
import * as _ from "lodash";
import { eventComparator } from "./util";
import FenwickTree from "./fenwickTree";

export function lcskPlusPlus(seqA: string, seqB: string, k: number): number {
  const dp = new Map<Pair, number>();
  const maxColDp = new FenwickTree();

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
      // TODO: dp(P) = k + max MaxColDp(x)
    } else {
    }
  });

  console.log(events);
  return 42;
}
