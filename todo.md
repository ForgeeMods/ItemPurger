# TODO: Integrate Configuration Options into the Mod

## General Settings
- [ ] **clearIntervalString & clearInterval**
    - Parse `clearIntervalString` into a usable time value (`clearInterval`) on load.
    - Use `clearInterval` to schedule periodic item purging.
- [ ] **enableWarnings**
    - Check this flag before scheduling or displaying any warning messages.
- [ ] **clearByAge**
    - Implement logic to clear items based on their age if this is enabled.
- [ ] **itemMaxAge**
    - Use this value to determine the maximum age an item can have before it is purged.

## Warning Settings
- [ ] **warningTimesStrings & warningTimes**
    - Parse `warningTimesStrings` into `warningTimes` (e.g., convert "10m" to a numeric time value).
    - Schedule warning messages at the parsed times before an item purge event.

## Item Settings
- [ ] **whitelist**
    - Ensure items in the whitelist are never purged.
- [ ] **blacklist**
    - Always purge items in the blacklist (if applicable).
- [ ] **protectedPlayers**
    - Implement logic to check if a player is in the protected list and prevent purging items near them.
- [ ] **enableSmartGrouping**
    - If enabled, group similar items together to optimize the purge process.
- [ ] **itemPriorityList**
    - Use this list to determine the order or priority of items to be purged (e.g., purge lower-priority items first).

## Dynamic Threshold Settings
- [ ] **enableDynamicThreshold**
    - Activate dynamic threshold logic if enabled.
- [ ] **tpsThreshold**
    - Monitor server TPS; if it falls below this threshold, adjust or delay item purging.
- [ ] **entityThreshold**
    - Use this value to determine when the entity count is too high and adjust purge behavior accordingly.

## Player Protection Settings
- [ ] **enablePlayerProtection**
    - If enabled, prevent item purging in a defined radius around players.
- [ ] **protectedRadius**
    - Apply this radius to determine the area around each player that should be protected.

## Undo Settings
- [ ] **enableUndo**
    - Implement an undo feature that allows restoration of purged items.
- [ ] **undoTimeLimit**
    - Use this setting to limit how long after a purge an undo operation can be performed.

## Effects Settings
- [ ] **enableClearSound & clearSound**
    - If enabled, play the specified `clearSound` when items are purged.
- [ ] **enableParticles & particleType**
    - If enabled, display the specified `particleType` effect when purging occurs.

## Admin Settings
- [ ] **enableAdminConfirmation**
    - Require admin confirmation for purging actions if enabled.
    - Implement a check that only allows an admin to bypass or confirm the purge process.

## Region Settings
- [ ] **enableRegionClearing**
    - If enabled, restrict the purge functionality to specific regions.
- [ ] **regionList**
    - Parse and apply the list of regions where purging is allowed or disallowed.
    - Ensure that the region logic integrates well with other mod features (e.g., player protection).

---

### Additional Notes
- Ensure that all time values (e.g., clearInterval, undoTimeLimit) are consistently parsed and formatted using `ItemPurgerUtils`.
- Add logging around each major action for easier debugging.
- Test each configuration change independently to verify that changes in the TOML file are correctly applied.
