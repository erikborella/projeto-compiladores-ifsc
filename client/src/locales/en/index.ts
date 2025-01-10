import { ResourceValues } from "../resource";
import { errorResources } from "./error";
import { indexResources } from "./indexPage";
import { examplesResources } from "./examples";
import { codeIdResources } from "./codeId";
import { tokenResources } from "./token";
import { syntaxResources } from "./syntax";
import { symbolsTableResources } from "./symbolsTable";

export const en: ResourceValues = {
  projectTitle: "Compiler Project",
  error: errorResources,
  index: indexResources,
  examples: examplesResources,
  codeId: codeIdResources,
  token: tokenResources,
  syntax: syntaxResources,
  symbolsTable: symbolsTableResources,
};
