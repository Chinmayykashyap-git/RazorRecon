# Razor Recon ML layer

The ML layer is intentionally isolated from financial decision-making. `train.py` trains a local `RandomForestClassifier` for candidate matching when deterministic evidence is incomplete.

## Reproduce the model

```text
cd ml
python -m pip install -r requirements.txt
python train.py
```

The script reads `data/ml-training.csv`, uses a stratified 80/20 holdout, prints accuracy, precision, recall, F1, ROC-AUC, a confusion matrix, and saves `model/matcher.joblib`.

Features:

- `amount_difference`
- `timestamp_difference_seconds`
- `merchant_match`
- `currency_match`
- `payment_method_match`
- `reference_similarity`

The saved artifact includes the feature order and thresholds for `probable_match` and `review_recommended`. Metrics printed by the script are evaluation metrics for that generated dataset, not production accuracy. The Java backend remains operational when this optional model is unavailable.
