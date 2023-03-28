import omitBy from "lodash/omitBy";
import { ALL_ENVIRONMENTS_VALUE } from "src/domain/environment";
import {
  RequestVerdictApproval,
  RequestVerdictDecline,
  RequestVerdictDelete,
} from "src/domain/requests/requests-types";
import { Team } from "src/domain/team";
import { ALL_TEAMS_VALUE } from "src/domain/team/team-types";
import {
  transformGetTopicAdvancedConfigOptionsResponse,
  transformGetTopicRequestsResponse,
  transformTopicApiResponse,
} from "src/domain/topic/topic-transformer";
import {
  TopicAdvancedConfigurationOptions,
  TopicApiResponse,
  TopicRequestApiResponse,
} from "src/domain/topic/topic-types";
import api from "src/services/api";
import {
  KlawApiRequest,
  KlawApiRequestQueryParameters,
  KlawApiResponse,
} from "types/utils";

const getTopics = async ({
  currentPage = 1,
  environment = "ALL",
  teamName,
  searchTerm,
}: {
  currentPage: number;
  environment: string;
  teamName: Team;
  searchTerm?: string;
}): Promise<TopicApiResponse> => {
  // "ALL_TEAMS_VALUE" represents topic list without
  // the optional team parameter
  // where we still need a way to represent an
  // option for "Select all teams" to users
  const team = teamName !== ALL_TEAMS_VALUE && teamName;

  const params: Record<string, string> = {
    pageNo: currentPage.toString(),
    env: environment || ALL_ENVIRONMENTS_VALUE,
    ...(team && { teamName: team }),
    ...(searchTerm && { topicnamesearch: searchTerm }),
  };

  return api
    .get<KlawApiResponse<"getTopics">>(
      `/getTopics?${new URLSearchParams(params)}`
    )
    .then(transformTopicApiResponse);
};

type GetTopicNamesArgs = Partial<{
  onlyMyTeamTopics: boolean;
  envSelected?: string;
}>;

const getTopicNames = async ({
  onlyMyTeamTopics,
  envSelected = "ALL",
}: GetTopicNamesArgs = {}) => {
  const isMyTeamTopics = onlyMyTeamTopics ?? false;
  const params = {
    isMyTeamTopics: isMyTeamTopics.toString(),
    envSelected,
  };

  return api.get<KlawApiResponse<"getTopicsOnly">>(
    `/getTopicsOnly?${new URLSearchParams(params)}`
  );
};

interface GetTopicTeamArgs {
  topicName: string;
  patternType?: "LITERAL" | "PREFIXED";
}

const getTopicTeam = async ({
  topicName,
  patternType = "LITERAL",
}: GetTopicTeamArgs) => {
  const params = { topicName, patternType };

  return api.get<KlawApiResponse<"getTopicTeam">>(
    `/getTopicTeam?${new URLSearchParams(params)}`
  );
};

const getTopicAdvancedConfigOptions = (): Promise<
  TopicAdvancedConfigurationOptions[]
> =>
  api
    .get<KlawApiResponse<"getAdvancedTopicConfigs">>("/getAdvancedTopicConfigs")
    .then(transformGetTopicAdvancedConfigOptionsResponse);

const requestTopic = (
  payload: KlawApiRequest<"createTopicsCreateRequest">
): Promise<unknown> => {
  return api.post<
    KlawApiResponse<"createTopicsCreateRequest">,
    KlawApiRequest<"createTopicsCreateRequest">
  >("/createTopics", payload);
};

const getTopicRequestsForApprover = (
  params: KlawApiRequestQueryParameters<"getTopicRequestsForApprover">
): Promise<TopicRequestApiResponse> => {
  const filteredParams = omitBy(
    { ...params, teamId: String(params.teamId) },
    (value, property) => {
      const omitTeamId = property === "teamId" && value === "undefined";
      const omitSearch = property === "search" && value === "";
      const omitEnv =
        property === "env" && (value === "ALL" || value === undefined);

      return omitTeamId || omitSearch || omitEnv;
    }
  );

  return api
    .get<KlawApiResponse<"getTopicRequestsForApprover">>(
      `/getTopicRequestsForApprover?${new URLSearchParams(filteredParams)}`
    )
    .then(transformGetTopicRequestsResponse);
};

const getTopicRequests = (
  params: KlawApiRequestQueryParameters<"getTopicRequests">
): Promise<TopicRequestApiResponse> => {
  const filteredParams = omitBy(
    { ...params, isMyRequest: String(Boolean(params.isMyRequest)) },
    (value, property) => {
      const omitIsMyRequest = property === "isMyRequest" && value !== "true"; // Omit if anything else than true
      const omitSearch = property === "search" && !value;
      const omitEnv =
        property === "env" && (value === "ALL" || value === undefined);
      const omitRequestOperationType =
        property === "operationType" &&
        (value === "ALL" || value === undefined);

      return (
        omitIsMyRequest || omitSearch || omitEnv || omitRequestOperationType
      );
    }
  );

  return api
    .get<KlawApiResponse<"getTopicRequests">>(
      `/getTopicRequests?${new URLSearchParams(filteredParams)}`
    )
    .then(transformGetTopicRequestsResponse);
};

const approveTopicRequest = ({
  reqIds,
}: {
  reqIds: RequestVerdictApproval<"SCHEMA">["reqIds"];
}) => {
  return api.post<
    KlawApiResponse<"approveRequest">,
    RequestVerdictApproval<"TOPIC">
  >(`/request/approve`, {
    reqIds,
    requestEntityType: "TOPIC",
  });
};

const declineTopicRequest = ({
  reqIds,
  reason,
}: Omit<RequestVerdictDecline<"TOPIC">, "requestEntityType">) => {
  return api.post<
    KlawApiResponse<"declineRequest">,
    RequestVerdictDecline<"TOPIC">
  >(`/request/decline`, {
    reqIds,
    reason,
    requestEntityType: "TOPIC",
  });
};

const deleteTopicRequest = ({
  reqIds,
}: Omit<RequestVerdictDelete<"TOPIC">, "requestEntityType">) => {
  return api.post<
    KlawApiResponse<"deleteRequest">,
    RequestVerdictDelete<"TOPIC">
  >(`/request/delete`, {
    reqIds,
    requestEntityType: "TOPIC",
  });
};

export {
  getTopics,
  getTopicNames,
  getTopicTeam,
  getTopicAdvancedConfigOptions,
  requestTopic,
  getTopicRequestsForApprover,
  getTopicRequests,
  approveTopicRequest,
  declineTopicRequest,
  deleteTopicRequest,
};
