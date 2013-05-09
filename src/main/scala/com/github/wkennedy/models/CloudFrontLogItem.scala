package com.github.wkennedy.models

class CloudFrontLogItem {
  //#Fields: date time x-edge-location sc-bytes c-ip cs-method cs(Host) cs-uri-stem sc-status cs(Referer) cs(User-Agent) cs-uri-query cs(Cookie) x-edge-result-type x-edge-request-id
  var date: String = _
  var time: String = _
  var x_edge_location: String = _
  var sc_bytes: String = _
  var c_ip: String = _
  var cs_method: String = _
  var cs_host: String = _
  var cs_uri_stem: String = _
  var sc_status: String = _
  var cs_referer: String = _
  var cs_user_agent: String = _
  var cs_uri_query: String = _
  var cs_cookie: String = _
  var x_edge_result_type: String = _
  var x_edge_request_id: String = _
}
