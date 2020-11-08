package com.websoftquality.findersseat.interfaces;

/**
 * @package com.trioangle.igniter
 * @subpackage interfaces
 * @category ServiceListener
 * @author Trioangle Product Team
 * @version 1.0
 **/

import com.websoftquality.findersseat.datamodels.main.JsonResponse;

/*****************************************************************
 ServiceListener
 ****************************************************************/
public interface ServiceListener {

    void onSuccess(JsonResponse jsonResp, String data);

    void onFailure(JsonResponse jsonResp, String data);
}
