/*
 * Copyright 2025 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.firestore.snippets;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Pipeline;
import com.google.cloud.firestore.PipelineSource;
import com.google.cloud.firestore.pipeline.expressions.AggregateFunction;
import com.google.cloud.firestore.pipeline.expressions.Expression;
import com.google.cloud.firestore.pipeline.expressions.Field;
import com.google.cloud.firestore.pipeline.stages.CollectionGroupOptions;
import com.google.cloud.firestore.pipeline.stages.CollectionHints;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class PipelineSnippets {

  private final Firestore db;

  public PipelineSnippets(Firestore db) {
    this.db = db;
  }

  public void loadTestData() throws ExecutionException, InterruptedException {
    // [START pipeline_join_test_data]
    // Load set of cities.
    CollectionReference cities = db.collection("cities");

    Map<String, Object> sfData = new HashMap<>();
    sfData.put("name", "San Francisco");
    sfData.put("state", "CA");
    sfData.put("country", "USA");
    cities.document("SF").set(sfData).get();

    Map<String, Object> laData = new HashMap<>();
    laData.put("name", "Los Angeles");
    laData.put("state", "CA");
    laData.put("country", "USA");
    cities.document("LA").set(laData).get();

    Map<String, Object> dcData = new HashMap<>();
    dcData.put("name", "Washington, D.C.");
    dcData.put("state", null);
    dcData.put("country", "USA");
    cities.document("DC").set(dcData).get();

    Map<String, Object> tokData = new HashMap<>();
    tokData.put("name", "Tokyo");
    tokData.put("state", null);
    tokData.put("country", "Japan");
    cities.document("TOK").set(tokData).get();

    // Load restaurants in various cities.
    CollectionReference sfRestaurants =
        db.collection("cities").document("SF").collection("restaurants");
    CollectionReference laRestaurants =
        db.collection("cities").document("LA").collection("restaurants");
    CollectionReference dcRestaurants =
        db.collection("cities").document("DC").collection("restaurants");

    Map<String, Object> rest1Data = new HashMap<>();
    rest1Data.put("name", "Golden Gate Pizza");
    rest1Data.put("type", "pizza");
    rest1Data.put("owner_id", "Mario Rossi");
    DocumentReference rest1 = sfRestaurants.add(rest1Data).get();

    Map<String, Object> rest2Data = new HashMap<>();
    rest2Data.put("name", "Bay Area Burger");
    rest2Data.put("type", "burger");
    rest2Data.put("owner_id", "Sarah Jenkins");
    DocumentReference rest2 = sfRestaurants.add(rest2Data).get();

    Map<String, Object> rest3Data = new HashMap<>();
    rest3Data.put("name", "Sunset Taco");
    rest3Data.put("type", "mexican");
    rest3Data.put("owner_id", "Edward");
    DocumentReference rest3 = sfRestaurants.add(rest3Data).get();

    Map<String, Object> rest4Data = new HashMap<>();
    rest4Data.put("name", "Hollywood Sushi");
    rest4Data.put("type", "sushi");
    rest4Data.put("owner_id", "Ken Kenji");
    DocumentReference rest4 = laRestaurants.add(rest4Data).get();

    Map<String, Object> rest5Data = new HashMap<>();
    rest5Data.put("name", "Venice Pizza");
    rest5Data.put("type", "pizza");
    rest5Data.put("owner_id", "Luigi Romano");
    DocumentReference rest5 = laRestaurants.add(rest5Data).get();

    Map<String, Object> rest6Data = new HashMap<>();
    rest6Data.put("name", "Capitol Tacos");
    rest6Data.put("type", "mexican");
    rest6Data.put("owner_id", "Maria Garcia");
    DocumentReference rest6 = dcRestaurants.add(rest6Data).get();

    Map<String, Object> rest7Data = new HashMap<>();
    rest7Data.put("name", "Georgetown Coffee");
    rest7Data.put("type", "cafe");
    rest7Data.put("owner_id", "David Kim");
    DocumentReference rest7 = dcRestaurants.add(rest7Data).get();

    // Load collection of reviews.
    CollectionReference reviews = db.collection("reviews");

    Map<String, Object> rev1 = new HashMap<>();
    rev1.put("restaurant", rest1);
    rev1.put("rating", 5);
    rev1.put("reviewer_id", "Alice");
    reviews.add(rev1).get();

    Map<String, Object> rev2 = new HashMap<>();
    rev2.put("restaurant", rest1);
    rev2.put("rating", 4);
    rev2.put("reviewer_id", "Bob");
    reviews.add(rev2).get();

    Map<String, Object> rev3 = new HashMap<>();
    rev3.put("restaurant", rest2);
    rev3.put("rating", 4);
    rev3.put("reviewer_id", "Charlie");
    reviews.add(rev3).get();

    Map<String, Object> rev4 = new HashMap<>();
    rev4.put("restaurant", rest3);
    rev4.put("rating", 5);
    rev4.put("reviewer_id", "Diana");
    reviews.add(rev4).get();

    Map<String, Object> rev5 = new HashMap<>();
    rev5.put("restaurant", rest3);
    rev5.put("rating", 4);
    rev5.put("reviewer_id", "Edward");
    reviews.add(rev5).get();

    Map<String, Object> rev6 = new HashMap<>();
    rev6.put("restaurant", rest3);
    rev6.put("rating", 4);
    rev6.put("reviewer_id", "Fiona");
    reviews.add(rev6).get();

    // rest4 has 0 reviews

    Map<String, Object> rev7 = new HashMap<>();
    rev7.put("restaurant", rest5);
    rev7.put("rating", 3);
    rev7.put("reviewer_id", "George");
    reviews.add(rev7).get();

    Map<String, Object> rev8 = new HashMap<>();
    rev8.put("restaurant", rest6);
    rev8.put("rating", 5);
    rev8.put("reviewer_id", "Hannah");
    reviews.add(rev8).get();

    Map<String, Object> rev9 = new HashMap<>();
    rev9.put("restaurant", rest6);
    rev9.put("rating", 4);
    rev9.put("reviewer_id", "Ian");
    reviews.add(rev9).get();

    Map<String, Object> rev10 = new HashMap<>();
    rev10.put("restaurant", rest7);
    rev10.put("rating", 5);
    rev10.put("reviewer_id", "Julia");
    reviews.add(rev10).get();
    // [END pipeline_join_test_data]
  }

  public ApiFuture<Pipeline.Snapshot> pipelineJoinLookup() {
    // [START pipeline_join_lookup]
    ApiFuture<Pipeline.Snapshot> results =
        db.pipeline()
            .collectionGroup("reviews")
            .define(Field.ofUserPath("restaurant").as("restaurant_name"))
            .addFields(
                db.pipeline()
                    .collectionGroup("restaurants")
                    .where(
                        Field.ofUserPath("__name__").equal(Expression.variable("restaurant_name")))
                    .select("name", "type")
                    .toScalarExpression()
                    .as("restaurant"))
            .execute();
    // [END pipeline_join_lookup]
    return results;
  }

  public ApiFuture<Pipeline.Snapshot> pipelineJoinArray() {
    // [START pipeline_join_array]
    ApiFuture<Pipeline.Snapshot> results =
        db.pipeline()
            .collectionGroup("restaurants")
            .where(Field.ofUserPath("type").equal("pizza"))
            .define(Field.ofUserPath("__name__").as("restaurant_name"))
            .select(
                Field.ofUserPath("name"),
                db.pipeline()
                    .collectionGroup("reviews")
                    .where(
                        Field.ofUserPath("restaurant")
                            .equal(Expression.variable("restaurant_name")))
                    .select("rating", "reviewer_id")
                    .toArrayExpression()
                    .as("reviews"))
            .execute();
    // [END pipeline_join_array]
    return results;
  }

  public ApiFuture<Pipeline.Snapshot> pipelineJoinAggregate() {
    // [START pipeline_join_aggregate]
    ApiFuture<Pipeline.Snapshot> results =
        db.pipeline()
            .collectionGroup("restaurants")
            .where(Field.ofUserPath("type").equal("pizza"))
            .define(Field.ofUserPath("__name__").as("restaurant_name"))
            .select(
                Field.ofUserPath("name"),
                db.pipeline()
                    .collectionGroup("reviews")
                    .where(
                        Field.ofUserPath("restaurant")
                            .equal(Expression.variable("restaurant_name")))
                    .aggregate(AggregateFunction.average("rating").as("avg_rating"))
                    .toScalarExpression()
                    .as("avg_rating"))
            .execute();
    // [END pipeline_join_aggregate]
    return results;
  }

  public ApiFuture<Pipeline.Snapshot> pipelineJoinLimit() {
    // [START pipeline_join_limit]
    ApiFuture<Pipeline.Snapshot> results =
        db.pipeline()
            .collectionGroup("restaurants")
            .define(Field.ofUserPath("__name__").as("restaurant_name"))
            .select(
                Field.ofUserPath("name"),
                db.pipeline()
                    .collectionGroup("reviews")
                    .where(
                        Field.ofUserPath("restaurant")
                            .equal(Expression.variable("restaurant_name")))
                    .sort(Field.ofUserPath("rating").descending())
                    .limit(2)
                    .select("rating", "reviewer_id")
                    .toArrayExpression()
                    .as("top_reviews"))
            .execute();
    // [END pipeline_join_limit]
    return results;
  }

  public ApiFuture<Pipeline.Snapshot> pipelineJoinSubcollection() {
    // [START pipeline_join_subcollection]
    ApiFuture<Pipeline.Snapshot> results =
        db.pipeline()
            .collection("cities")
            .addFields(
                PipelineSource.subcollection("restaurants")
                    .toArrayExpression()
                    .length()
                    .as("restaurant_count"))
            .execute();
    // [END pipeline_join_subcollection]
    return results;
  }

  public ApiFuture<Pipeline.Snapshot> pipelineJoinMultiField() {
    // [START pipeline_join_multi_field]
    ApiFuture<Pipeline.Snapshot> results =
        db.pipeline()
            .collectionGroup("restaurants")
            .define(
                Field.ofUserPath("owner_id").as("owner_id"),
                Field.ofUserPath("__name__").as("__name__"))
            .where(
                db.pipeline()
                    .collectionGroup("reviews")
                    .where(Field.ofUserPath("restaurant").equal(Expression.variable("__name__")))
                    .where(Field.ofUserPath("reviewer_id").equal(Expression.variable("owner_id")))
                    .aggregate(AggregateFunction.countAll().as("c"))
                    .toScalarExpression()
                    .greaterThan(0))
            .execute();
    // [END pipeline_join_multi_field]
    return results;
  }

  public ApiFuture<Pipeline.Snapshot> pipelineJoinAnti() {
    // [START pipeline_join_anti]
    ApiFuture<Pipeline.Snapshot> results =
        db.pipeline()
            .collectionGroup("restaurants")
            .define(Field.ofUserPath("__name__").as("restaurant_name"))
            .where(
                db.pipeline()
                    .collectionGroup("reviews")
                    .where(
                        Field.ofUserPath("restaurant")
                            .equal(Expression.variable("restaurant_name")))
                    .aggregate(AggregateFunction.countAll().as("review_count"))
                    .toScalarExpression()
                    .equal(0))
            .execute();
    // [END pipeline_join_anti]
    return results;
  }

  public ApiFuture<Pipeline.Snapshot> pipelineJoinUnnest() {
    // [START pipeline_join_unnest]
    ApiFuture<Pipeline.Snapshot> results =
        db.pipeline()
            .collectionGroup("restaurants")
            .where(Field.ofUserPath("type").equal("pizza"))
            .define(Field.ofUserPath("__name__").as("restaurant_name"))
            .unnest(
                db.pipeline()
                    .collectionGroup("reviews")
                    .where(
                        Field.ofUserPath("restaurant")
                            .equal(Expression.variable("restaurant_name")))
                    .select("rating", "reviewer_id")
                    .toArrayExpression()
                    .as("review"))
            .execute();
    // [END pipeline_join_unnest]
    return results;
  }

  public ApiFuture<Pipeline.Snapshot> pipelineJoinUncorrelated() {
    // [START pipeline_join_uncorrelated]
    ApiFuture<Pipeline.Snapshot> results =
        db.pipeline()
            .collection("reviews")
            // Average review rating is 4.3
            .where(
                Field.ofUserPath("rating")
                    .greaterThan(
                        db.pipeline()
                            .collection("reviews")
                            .aggregate(AggregateFunction.average("rating").as("avg"))
                            .toScalarExpression()))
            .select("rating", "reviewer_id")
            .execute();
    // [END pipeline_join_uncorrelated]
    return results;
  }

  public ApiFuture<Pipeline.Snapshot> pipelineForceTableScan() {
    // [START pipeline_force_table_scan]
    // Force Planner to only do a Full-Table Scan
    ApiFuture<Pipeline.Snapshot> results =
        db.pipeline()
            .collectionGroup(
                "customers",
                new CollectionGroupOptions()
                    .withHints(new CollectionHints().withForceIndex("primary")))
            .limit(100)
            .execute();
    // [END pipeline_force_table_scan]
    return results;
  }
}
