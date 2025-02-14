import { Box, Card, Grid } from "@aivenio/aquarium";
import StatsDisplay from "src/app/features/topics/details/components/StatsDisplay";

type SchemaStatsProps = {
  version: number;
  id: number;
  compatibility: string;
  isLoading: boolean;
};
function SchemaStats({
  isLoading,
  version,
  id,
  compatibility,
}: SchemaStatsProps) {
  return (
    <Grid
      cols={"2"}
      rows={"1"}
      gap={"l2"}
      style={{ gridTemplateRows: "auto" }}
      marginTop={"l2"}
    >
      <Grid.Item xs={2}>
        <Card title="" fullWidth>
          <Box.Flex display="flex" gap={"l7"}>
            <StatsDisplay
              isLoading={isLoading}
              amount={version}
              entity={"Version no."}
            />
            <StatsDisplay isLoading={isLoading} amount={id} entity={"ID"} />
            <StatsDisplay
              isLoading={isLoading}
              chip={{ status: "info", text: compatibility }}
              entity={"Compatibility"}
            />
          </Box.Flex>
        </Card>
      </Grid.Item>
    </Grid>
  );
}

export { SchemaStats };
