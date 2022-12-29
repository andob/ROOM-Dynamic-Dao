package ro.dobrescuandrei.roomdynamicdao

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses
(
    SQLEscapeTests::class,
    QueryJoinAndProjectionClausesTests::class,
    QueryWhereConditionsTests::class,
    FunctionalRestaurantListQueryBuilderTests::class,
    RestaurantJoinListQueryBuilderTests::class,
    RestaurantListQueryBuilderTests::class,
)
class TestSuite
