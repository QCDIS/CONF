# coding: utf-8

from __future__ import absolute_import
from datetime import date, datetime  # noqa: F401

from typing import List, Dict  # noqa: F401

from nl.sne.sure_tosca.models.base_model_ import Model
from nl.sne.sure_tosca import util


class ArrayOfNodeTemplate(Model):
    """NOTE: This class is auto generated by the swagger code generator program.

    Do not edit the class manually.
    """

    def __init__(self):  # noqa: E501
        """ArrayOfNodeTemplate - a model defined in Swagger

        """
        self.swagger_types = {
        }

        self.attribute_map = {
        }

    @classmethod
    def from_dict(cls, dikt):
        """Returns the dict as a model

        :param dikt: A dict.
        :type: dict
        :return: The ArrayOfNodeTemplate of this ArrayOfNodeTemplate.  # noqa: E501
        :rtype: ArrayOfNodeTemplate
        """
        return util.deserialize_model(dikt, cls)