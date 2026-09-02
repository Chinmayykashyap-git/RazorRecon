"""Train a conservative local Random Forest on generated reconciliation evidence."""
from pathlib import Path

import joblib
import pandas as pd
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score, classification_report, confusion_matrix, precision_score, recall_score, f1_score, roc_auc_score
from sklearn.model_selection import train_test_split

ROOT = Path(__file__).resolve().parents[1]
DATASET = ROOT / "data" / "ml-training.csv"
MODEL = ROOT / "ml" / "model" / "matcher.joblib"
FEATURES = ["amount_difference", "timestamp_difference_seconds", "merchant_match", "currency_match", "payment_method_match", "reference_similarity"]


def main():
    data = pd.read_csv(DATASET)
    for column in ("merchant_match", "currency_match", "payment_method_match"):
        data[column] = data[column].astype(int)
    x_train, x_test, y_train, y_test = train_test_split(
        data[FEATURES], data["label"], test_size=0.2, random_state=42, stratify=data["label"]
    )
    model = RandomForestClassifier(n_estimators=200, min_samples_leaf=3, class_weight="balanced", random_state=42, n_jobs=-1)
    model.fit(x_train, y_train)
    predictions = model.predict(x_test)
    probabilities = model.predict_proba(x_test)[:, 1]
    print(f"accuracy={accuracy_score(y_test, predictions):.4f}")
    print(f"precision={precision_score(y_test, predictions, zero_division=0):.4f}")
    print(f"recall={recall_score(y_test, predictions, zero_division=0):.4f}")
    print(f"f1={f1_score(y_test, predictions, zero_division=0):.4f}")
    print(f"roc_auc={roc_auc_score(y_test, probabilities):.4f}")
    print("confusion_matrix=")
    print(confusion_matrix(y_test, predictions))
    print(classification_report(y_test, predictions, zero_division=0))
    MODEL.parent.mkdir(parents=True, exist_ok=True)
    joblib.dump({"model": model, "features": FEATURES, "thresholds": {"probable_match": 0.95, "review_recommended": 0.75}}, MODEL)
    print(f"saved={MODEL}")


if __name__ == "__main__":
    main()
