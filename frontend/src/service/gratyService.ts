import { client } from "@/utils/client.ts";
import type { GratyFunction } from "@/interfaces/utils.ts";

class GratyService {
  getFunctions() {
    return client.get<Array<GratyFunction>>("/graty/utils/functions");
  }
}

export const gratyService = new GratyService();